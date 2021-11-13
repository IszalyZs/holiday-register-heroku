package com.oh.register.controller;

import com.oh.register.config.BindingErrorHandler;
import com.oh.register.exception.RegisterException;
import com.oh.register.model.dto.ChildrenDTO;
import com.oh.register.model.dto.MessageDTO;
import com.oh.register.service.ChildrenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/children")
@Tag(name = "Operations on Children")
public class ChildrenController {

    private final ChildrenService childrenService;
    private final BindingErrorHandler bindingErrorHandler;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ChildrenController(ChildrenService childrenService, BindingErrorHandler bindingErrorHandler) {
        this.childrenService = childrenService;
        this.bindingErrorHandler = bindingErrorHandler;
    }

    @GetMapping("/all")
    @Operation(summary = "list all children", description = "list all children")
    public ResponseEntity<List<ChildrenDTO>> findAll() {
        return ResponseEntity.ok(childrenService.findAll());
    }


    @DeleteMapping("/{id}/delete")
    @Operation(summary = "delete children by id", description = "delete children by id")
    public ResponseEntity<MessageDTO> deleteById(@PathVariable("id") Long id) {
        childrenService.deleteById(id);
        String message="The entity was deleted with id: " + id + "!";
        return ResponseEntity.ok(new MessageDTO(message));
    }

    @GetMapping("/{id}/get")
    @Operation(summary = "list children by id", description = "list children by id")
    public ResponseEntity<ChildrenDTO> findById(@PathVariable("id") Long id) {
        if(id==null) throw new RegisterException("The given id mustn't be null!");
        return ResponseEntity.ok(childrenService.findById(id));
    }

    @PostMapping("/employee/{id}/add")
    @Operation(summary = "save children", description = "save children")
    public ResponseEntity<ChildrenDTO> save(@Valid @RequestBody ChildrenDTO childrenDTO, BindingResult bindingResult,@PathVariable("id") Long id) {
        if (childrenDTO.getId() != null) childrenDTO.setId(null);
        childrenDTO.setEmployeeId(id);
        String logMessage = "Posted children entity contains error(s): ";
        bindingErrorHandler.bindingResult(bindingResult, logMessage, logger);
        return ResponseEntity.ok(childrenService.save(childrenDTO));
    }

    @PutMapping("/{id}/update")
    @Operation(summary = "update children by id", description = "update children by id")
    public ResponseEntity<ChildrenDTO> update(@Valid @RequestBody ChildrenDTO childrenDTO, BindingResult bindingResult, @PathVariable("id") Long id) {
        String logMessage = "Updated children entity contains error(s): ";
        bindingErrorHandler.bindingResult(bindingResult, logMessage, logger);
        childrenDTO.setId(id);
        return ResponseEntity.ok(childrenService.update(childrenDTO));
    }


}
