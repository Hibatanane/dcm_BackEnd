package prjt.dcm.Controllers;

import com.cloudinary.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prjt.dcm.Dto.ApiResponse;
import prjt.dcm.Dto.RoleDTO;
import prjt.dcm.Entities.Role;
import prjt.dcm.Repositories.RoleRepository;
import prjt.dcm.Services.RoleService;

import java.util.List;

@RequestMapping("/role")
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/getAllRoles")
    public ResponseEntity<List<RoleDTO>> recupererRoles() {
        System.out.println("Execution getALLrOLES");
        List<RoleDTO> roles = roleService.recupererRoles();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(roles, headers, HttpStatus.OK);
    }

    @DeleteMapping("/supprimerRole")
    public ApiResponse supprimerRole(@RequestBody List<Long> idRole) {
        roleService.supprimerRole(idRole);
        return new ApiResponse("supprimé", 200);
    }

    @GetMapping("/recupererRole/{idRole}")
    public ResponseEntity<RoleDTO> recupererRole(@PathVariable long idRole) {
        System.out.println("execution de méthode get role by id ");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(roleService.recupererRole(idRole), headers, HttpStatus.OK);
    }

    @PostMapping("/ajouterRole")
    public ResponseEntity<ApiResponse> ajouterRole(@RequestBody RoleDTO roleDTO) {
        ApiResponse apiResponse = roleService.ajouterRole(roleDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (apiResponse.getCode() == 409) {
            return new ResponseEntity<>(apiResponse, headers, HttpStatus.OK);

        } else {
            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(apiResponse);

        }
    }

    @PostMapping("/modifierRole")
    public ResponseEntity<ApiResponse> modifierRole(@RequestBody RoleDTO roleDTO) {
        ApiResponse apiResponse = roleService.modifierRole(roleDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(apiResponse, headers, HttpStatus.OK);
    }
}
