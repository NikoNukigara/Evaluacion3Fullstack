package com.saludplus.citaservice.assembler;

import com.saludplus.citaservice.controller.CitaControllerV2;
import com.saludplus.citaservice.model.Cita;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CitaModelAssembler implements RepresentationModelAssembler<Cita, EntityModel<Cita>> {

    @Override
    public EntityModel<Cita> toModel(Cita cita) {
        return EntityModel.of(cita,
                linkTo(methodOn(CitaControllerV2.class).obtenerPorId(cita.getId())).withSelfRel(),
                linkTo(methodOn(CitaControllerV2.class).listarTodas()).withRel("citas"));
    }
}
