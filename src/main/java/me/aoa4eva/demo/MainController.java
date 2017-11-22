package me.aoa4eva.demo;

import com.cloudinary.utils.ObjectUtils;
import me.aoa4eva.demo.Configuration.CloudinaryConfig;
import me.aoa4eva.demo.models.Actor;
import me.aoa4eva.demo.models.Movie;
import me.aoa4eva.demo.repositories.ActorRepository;
import me.aoa4eva.demo.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    ActorRepository actorRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String showIndex(Model model)
    {
        model.addAttribute("gotmovies",movieRepository.count());
        model.addAttribute("gotactors",actorRepository.count());
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
        return "redirect:/";
    }

    @GetMapping("/addactor")
    public String addActor(Model model)
    {
        model.addAttribute("actor",new Actor());
        return "addactor";
    }

    @PostMapping("/addactor")
    public String saveActor(@ModelAttribute("actor") Actor actor)
    {
        actorRepository.save(actor);
        return "redirect:/";
    }

//    @PostMapping("/add")
//    This is where the magic happens. Once a user clicks 'submit ' on form.html then the details are submitted to
//    the controller. Here, the file (passed as a request parameter, as it is not part of the model) is uploaded to
//    cloudinaty.
//
//    The resulting image is saved into a Map called uploadResult(to prepare it for Cloudinary).
//
//            ObjectUtils.asMap('resourcetype',"auto") indicates that Cloudinary should automatically try to detect what kind
//    of file has been uploaded.
//
//    Once the file has been successfully saved on the Cloudinary server, and a URL is available, it is saved to the
//    actor object (using the setter for headshot), and the actor model is saved to the database, with the name
//    entered by the user and the file uploaded. 'The user is then returned to the default route and should see the list
//    of actors and their headshots.



    @GetMapping("/addactorstomovie/{id}")
    public String addActor(@PathVariable("id") long movieID, Model model)
    {
        model.addAttribute("mov",movieRepository.findOne(new Long(movieID)));
        model.addAttribute("actorList",actorRepository.findAll());
        return "addactor";
    }

    @PostMapping("/addactor/{id}")
    public String processActor(@PathVariable("actor") Actor actor, @RequestParam("file")MultipartFile file){
        if (file.isEmpty()){
            return "redirect:/addactor";
        }
        try{
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype","auto"));
            actor.setHeadshot(uploadResult.get("url").toString());
            System.out.println(actor.getHeadshot());
            actorRepository.save(actor);
        } catch (IOException e){
            e.printStackTrace();
            return "redirect:/addactor";
        }

        return "index";
    }

//    @PostMapping("/actorstomovie/{id}")
//    public String addActor(@ModelAttribute("mov") String mov, HttpServletRequest servletRequest)
//    {
//        return "redirect:/";
//
//    }

    @GetMapping("/addmoviestoactor/{id}")
    public String addMovie(@PathVariable("id") long actorID, Model model)
    {
        model.addAttribute("actor",actorRepository.findOne(new Long(actorID)));
        model.addAttribute("movieList",movieRepository.findAll());
        return "movieaddactor";
    }

//    @PostMapping("/addactorstomovie")
//    public String addActorsToMovie(@ModelAttribute("anActor") Actor a, @ModelAttribute("mov") Movie m,Model model) {
//        model.addAttribute("actorList", actorRepository.findAll());
//        model.addAttribute("movieList", movieRepository.findAll());
//        return "redirect:/";
//    }

//    tkljglkfjfg
    @RequestMapping("/search")
    public String SearchResult() {
        //Get actors matching a string
        Iterable<Actor> actors = actorRepository.findAllByRealnameContainingIgnoreCase("Sandra");

        for (Actor a : actors) {
            System.out.println(a.getName());
        }

        //Show the movies the actors were in
        for (Movie m : movieRepository.findAllByCastIsIn(actors)) {
            System.out.println(m.getTitle());
        }
        return "redirect:/";
    }

}
