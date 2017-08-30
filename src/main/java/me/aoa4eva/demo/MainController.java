package me.aoa4eva.demo;

import me.aoa4eva.demo.models.Actor;
import me.aoa4eva.demo.models.Movie;
import me.aoa4eva.demo.repositories.ActorRepository;
import me.aoa4eva.demo.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {
    @Autowired
    ActorRepository actorRepository;

    @Autowired
    MovieRepository movieRepository;

    @RequestMapping("/")
    public String showIndex(Model model)
    {

        model.addAttribute("actorList",actorRepository.findAll());
        model.addAttribute("movieList",movieRepository.findAll());
        return "index";
    }

    @GetMapping("/addmovie")
    public String addMovie(Model model)
    {
        Movie movie = new Movie();
        model.addAttribute("movie",movie);
        return "addmovie";
    }

    @PostMapping("/addmovie")
    public String saveMovie(@ModelAttribute("movie") Movie movie)
    {
        movieRepository.save(movie);
        return "index";
    }

    @GetMapping("/addactor")
    public String addActor(Model model)
    {
        model.addAttribute("actor",new Actor());
        return "addactor";
    }

    @PostMapping("/addactor")
    public String saveMovie(@ModelAttribute("movie") Movie movie)
    {
        movieRepository.save(movie);
        return "index";
    }

    @GetMapping("/addactorstomovie/{id}")
    public String addActor(@PathVariable("id") long movieID, Model model)
    {
        model.addAttribute("mov",movieRepository.findOne(new Long(movieID)));
        model.addAttribute("actorList",actorRepository.findAll());
        return "movieaddactor";
    }


    @PostMapping("/actorstomovie/{id}")
    public String addActor(@ModelAttribute("mov") String mov, HttpServletRequest servletRequest)
    {
        return "index";

    }

    @GetMapping("/addactorstomovie")
    public String showAddActorsToMovie(Model model){
        model.addAttribute("actorlist",actorRepository.findAll());
        return "movieaddactor";
    }

    @PostMapping("/addactorstomovie")
    public String addActorsToMovie(@ModelAttribute("anActor") Actor a, @ModelAttribute("mov") Movie m,Model model)
    {
        model.addAttribute("actorList",actorRepository.findAll());
        model.addAttribute("movieList",movieRepository.findAll());
        return "index";
    }


    @PostMapping("/addmoviestoactor/{movid}")
    public String addMoviesToActor(@RequestParam("actors") String actorID, @PathVariable("movid") long movieID, Model model)
    {
        System.out.println("Actor ID"+actorID);
        System.out.println("Movie ID"+movieID);
        Movie m = movieRepository.findOne(new Long(movieID));
        m.addActor(actorRepository.findOne(new Long(actorID)));
        movieRepository.save(m);
        model.addAttribute("actorList",actorRepository.findAll());
        model.addAttribute("movieList",movieRepository.findAll());
        return "index";
    }



}
