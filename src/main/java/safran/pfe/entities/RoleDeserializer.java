/*package safran.pfe.entities;
import java.io.IOException;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import safran.pfe.entities.Role;
import safran.pfe.repo.RoleRepository;

@Component
public class RoleDeserializer extends JsonDeserializer<Role> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Long idLong = node.asLong();
        return roleRepository.findById(idLong.intValue())
                .orElseThrow(() -> new EntityNotFoundException("Role not found with ID: " + idLong));
    }
}
*/