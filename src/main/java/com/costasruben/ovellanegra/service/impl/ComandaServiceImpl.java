package com.costasruben.ovellanegra.service.impl;

import com.costasruben.ovellanegra.dto.ComandaDto;
import com.costasruben.ovellanegra.dto.CrearComandaRequest;
import com.costasruben.ovellanegra.dto.LineaComandaDto;
import com.costasruben.ovellanegra.enums.EstadoComanda;
import com.costasruben.ovellanegra.enums.EstadoMesa;
import com.costasruben.ovellanegra.exception.RestauranteException;
import com.costasruben.ovellanegra.service.ComandaService;
import com.costasruben.ovellanegra.service.InventarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.costasruben.ovellanegra.entities.*;
import com.costasruben.ovellanegra.repository.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComandaServiceImpl implements ComandaService {

    private final ComandaRepository comandaRepository;
    private final MesaRepository mesaRepository;
    private final PlatoRepository platoRepository;
    private final InventarioService inventarioService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public ComandaDto crearComanda(CrearComandaRequest request) {
        Mesa mesa = mesaRepository.findById(request.getMesaId())
                .orElseThrow(() -> new EntityNotFoundException("Mesa no encontrada: " + request.getMesaId()));

        if (mesa.getEstado() == EstadoMesa.ESPERANDO_COBRO) {
            throw new RestauranteException("La mesa está esperando cobro y no puede recibir nuevos pedidos.");
        }

        Comanda comanda = Comanda.builder()
                .mesa(mesa)
                .notas(request.getNotas())
                .estado(EstadoComanda.PENDIENTE)
                .build();

        for (CrearComandaRequest.LineaRequest lr : request.getLineas()) {
            Plato plato = platoRepository.findById(lr.getPlatoId())
                    .orElseThrow(() -> new EntityNotFoundException("Plato no encontrado: " + lr.getPlatoId()));
            if (!plato.getDisponible()) {
                throw new RestauranteException("El plato '" + plato.getNombre() + "' no está disponible.");
            }
            LineaComanda linea = LineaComanda.builder()
                    .comanda(comanda)
                    .plato(plato)
                    .cantidad(lr.getCantidad())
                    .precioUnitario(plato.getPrecio())
                    .observaciones(lr.getObservaciones())
                    .build();
            comanda.getLineas().add(linea);
        }

        Comanda saved = comandaRepository.save(comanda);

        // Marcar mesa como OCUPADA si estaba libre
        if (mesa.getEstado() == EstadoMesa.LIBRE) {
            mesa.setEstado(EstadoMesa.OCUPADA);
            mesaRepository.save(mesa);
        }

        ComandaDto dto = toDto(saved);

        // ── Notificación en tiempo real a la pantalla de cocina ──
        messagingTemplate.convertAndSend("/topic/cocina", dto);
        log.info("Comanda #{} enviada a cocina para mesa {}", saved.getId(), mesa.getNumero());

        return dto;
    }

    @Override
    @Transactional
    public ComandaDto cambiarEstado(Long comandaId, EstadoComanda nuevoEstado) {
        Comanda comanda = findById(comandaId);
        comanda.setEstado(nuevoEstado);
        Comanda saved = comandaRepository.save(comanda);

        // Si el cocinero confirma que la comanda está lista, descontar stock
        if (nuevoEstado == EstadoComanda.LISTA) {
            inventarioService.descontarStockPorComanda(comandaId);
        }

        ComandaDto dto = toDto(saved);

        // Notificar actualización a cocina y a la mesa correspondiente
        messagingTemplate.convertAndSend("/topic/cocina", dto);
        messagingTemplate.convertAndSend("/topic/mesa/" + comanda.getMesa().getId(), dto);

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComandaDto> obtenerComandasActivas() {
        return comandaRepository.findComandasActivas().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComandaDto> obtenerPorMesa(Long mesaId) {
        return comandaRepository.findByMesaId(mesaId).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ComandaDto obtenerPorId(Long id) {
        return toDto(findById(id));
    }

    // ── helpers ───────────────────────────────────────────────

    private Comanda findById(Long id) {
        return comandaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comanda no encontrada: " + id));
    }

    private ComandaDto toDto(Comanda c) {
        ComandaDto dto = new ComandaDto();
        dto.setId(c.getId());
        dto.setMesaId(c.getMesa().getId());
        dto.setMesaNumero(c.getMesa().getNumero());
        dto.setFechaHora(c.getFechaHora());
        dto.setEstado(c.getEstado());
        dto.setNotas(c.getNotas());
        dto.setLineas(c.getLineas().stream().map(this::lineaToDto).toList());
        return dto;
    }

    private LineaComandaDto lineaToDto(LineaComanda l) {
        LineaComandaDto dto = new LineaComandaDto();
        dto.setId(l.getId());
        dto.setPlatoId(l.getPlato().getId());
        dto.setPlatoNombre(l.getPlato().getNombre());
        dto.setCantidad(l.getCantidad());
        dto.setPrecioUnitario(l.getPrecioUnitario());
        dto.setObservaciones(l.getObservaciones());
        return dto;
    }

}