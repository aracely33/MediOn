package clinica.medtech.users.service;

import clinica.medtech.appointments.dto.response.AppointmentResponse;
import clinica.medtech.appointments.entity.Appointment;
import clinica.medtech.appointments.mapper.AppointmentMapper;
import clinica.medtech.appointments.repository.AppointmentRepository;
import clinica.medtech.common.dto.PaginatedResponse;
import clinica.medtech.exceptions.ProfessionalNotFoundException;
import clinica.medtech.users.Enum.EnumRole;
import clinica.medtech.users.dtoResponse.ProfessionalResponseDto;
import clinica.medtech.users.entities.ProfessionalModel;
import clinica.medtech.users.mapper.ProfessionalMapper;
import clinica.medtech.users.repository.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final AppointmentRepository appointmentRepository;
    private final ProfessionalMapper professionalMapper;
    private final AppointmentMapper appointmentMapper;

    //Obtener todos los profesionales creados en la base de datos de forma paginada

    public PaginatedResponse<ProfessionalResponseDto> getAllProfessionals(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProfessionalModel> professionals = professionalRepository.findAll(pageable);

        List<ProfessionalResponseDto> content = professionalMapper.mapToDtoList(professionals.getContent());

        return new PaginatedResponse<>(
                content,
                professionals.getNumber(),
                professionals.getSize(),
                professionals.getTotalPages(),
                professionals.getTotalElements()
        );
    }

    //Obtener profesional por id

    public ProfessionalResponseDto getProfessional(Long id) {
        ProfessionalModel professional = professionalRepository.findById(id)
                .orElseThrow(() -> new ProfessionalNotFoundException("Profesional no encontrado"));
        return professionalMapper.mapToDto(professional);
    }


    //  Obtener profesional por matrícula (única)
    public ProfessionalResponseDto getByMedicalLicense(String license) {
        ProfessionalModel professional = professionalRepository.findByMedicalLicense(license)
                .orElseThrow(() -> new ProfessionalNotFoundException("Profesional no encontrado con matrícula: " + license));
        return professionalMapper.mapToDto(professional);
    }

    //  Obtener profesional por email (único)
    public ProfessionalResponseDto getByEmail(String email) {
        ProfessionalModel professional = professionalRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ProfessionalNotFoundException("Profesional no encontrado con email: " + email));
        return professionalMapper.mapToDto(professional);
    }

    //  Buscar por nombre (coincidencia parcial)
    public List<ProfessionalResponseDto> searchByName(String name) {
        List<ProfessionalModel> professionals = professionalRepository.findByNameContainingIgnoreCase(name);

        if (professionals.isEmpty()) {
            throw new ProfessionalNotFoundException("No se encontraron profesionales con el nombre: " + name);
        }

        return professionalMapper.mapToDtoList(professionals);
    }

    //  Listar por especialidad con paginación
    public PaginatedResponse<ProfessionalResponseDto> getBySpecialty(String specialty, int page, int size) {
        Page<ProfessionalModel> professionals = professionalRepository.findBySpecialtyContainingIgnoreCase(
                specialty, PageRequest.of(page, size)
        );

        List<ProfessionalResponseDto> content = professionalMapper.mapToDtoList(professionals.getContent());

        return new PaginatedResponse<>(
                content,
                professionals.getNumber(),
                professionals.getSize(),
                professionals.getTotalPages(),
                professionals.getTotalElements()
        );
    }

    public List<AppointmentResponse> listAppointmentsPatientsByProfessionalId(Long id) {
        Optional<ProfessionalModel> professionalModel = professionalRepository.findById(id);

        if (professionalModel.isEmpty()) {
            throw new ProfessionalNotFoundException("Profesional no encontrado");
        }

        List<Appointment> appointmentsOfPatients = appointmentRepository.findByDoctorId(id);

        return appointmentMapper.toAppointmentListDto(appointmentsOfPatients);
    }




}
