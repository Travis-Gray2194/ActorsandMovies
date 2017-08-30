package me.aoa4eva.demo.repositories;

import me.aoa4eva.demo.models.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie,Long> {
}
