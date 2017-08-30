package me.aoa4eva.demo.repositories;

import me.aoa4eva.demo.models.Actor;
import org.springframework.data.repository.CrudRepository;

public interface ActorRepository extends CrudRepository<Actor,Long> {
}
