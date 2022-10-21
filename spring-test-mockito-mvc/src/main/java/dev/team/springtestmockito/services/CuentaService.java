package dev.team.springtestmockito.services;

import java.math.BigDecimal;
import java.util.List;

import dev.team.springtestmockito.models.Cuenta;

public interface CuentaService {

    List<Cuenta> findAll();

    Cuenta findById(Long id);

    Cuenta save(Cuenta cuenta);

    int revisarTotalTransferencias(Long bancoId);

    BigDecimal revisarSaldo(Long cuentaId);

    void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto,
            Long bancoId);
}
