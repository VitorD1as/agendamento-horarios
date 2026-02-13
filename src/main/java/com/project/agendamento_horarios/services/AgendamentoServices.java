package com.project.agendamento_horarios.services;

import com.project.agendamento_horarios.infrastructure.entity.Agendamento;
import com.project.agendamento_horarios.infrastructure.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AgendamentoServices {
    private final AgendamentoRepository agendamentoRepository;

    public Agendamento salvarAgendamento(Agendamento agendamento){
        LocalDateTime horaAgendamento = agendamento.getDataHoraAgendamento();
        LocalDateTime horaFim = agendamento.getDataHoraAgendamento().plusHours(1);

        Agendamento agendados = agendamentoRepository.findByServicoAndDataHoraAgendamentoBetween(agendamento.getServico(), horaAgendamento, horaFim);

        if(Objects.nonNull(agendados)){
            throw new RuntimeException("O horário já está preenchido");
        }
            return agendamentoRepository.save(agendamento);
        }

        public void deletarAgendamento(LocalDateTime dataHoraAgendamento, String cliente){
            agendamentoRepository.deleteByDataHoraAgendamentoAndCliente(dataHoraAgendamento, cliente);
        }

        public Agendamento buscarAgendamentosDia(LocalDate data){
            LocalDateTime primeiraHoraDoDia = data.atStartOfDay();
            LocalDateTime horaFinalDia = data.atTime(23,59,59);

            return agendamentoRepository.findByDataHoraAgendamentoBetween(primeiraHoraDoDia, horaFinalDia);
        }

        public Agendamento alterarAgendamento(Agendamento agendamento, String cliente, LocalDateTime dataHoraAgendamento){
            Agendamento agenda = agendamentoRepository.findByDataHoraAgendamentoAndCliente(dataHoraAgendamento, cliente);

            if(Objects.isNull(agenda)){
                throw new RuntimeException("Horário não está preenchido");
            }

            agendamento.setId(agenda.getId());
            return agendamentoRepository.save(agendamento);
        }
    }

