package com.template.sbtemplate.controller;

import com.template.sbtemplate.dto.EmployeeDto;
import com.template.sbtemplate.dto.Scope;
import com.template.sbtemplate.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@Tag(name = "Employee", description = "Employee management APIs")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Operation(summary = "Create a new employee")
    @PostMapping
    public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeDto emnployeeDto) {
        return ResponseEntity.ok(employeeService.create(emnployeeDto));
    }

    @Operation(
            summary = "Get an employee by ID",
            description = "Returns the employee with the given ID. Returns 404 if not found.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Employee found",
                            content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
                    @ApiResponse(responseCode = "404", description = "Employee not found",
                            content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> get(@PathVariable Long id, @RequestParam(required = false) Scope scope) {
        return employeeService.get(id, scope)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @Operation(summary = "Update an employee")
//    @PutMapping("/{id}")
//    public EmployeeDTO update(@PathVariable Long id, @RequestBody EmployeeDTO dto) {
//        Employee employee = mapToEntity(dto);
//        Employee updated = service.update(id, employee);
//        return mapToDTO(updated, dto.addresses != null);
//    }
//
//    @Operation(summary = "Delete an employee")
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id) {
//        service.delete(id);
//    }
//
//    // Mapping methods (implement as needed)
//    private Employee mapToEntity(EmployeeDTO dto) {
//        // Map DTO to entity (implement as needed)
//        return null;
//    }
//
//    private EmployeeDTO mapToDTO(Employee employee, boolean includeAddresses) {
//        // Map entity to DTO, include addresses if requested
//        return null;
//    }
}