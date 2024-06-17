package hu.gde.aycbph.controller;

import hu.gde.aycbph.entity.Story;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Locale;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class HomeController {

    public String Stories(Model model, Locale locale){
        model.addAttribute("pageTitle", "Bármi");
        model.addAttribute("stories", getStories());
        System.out.println(String.format("Request rececired.langue: %s, Contry:%s %n",
                locale.getLanguage(), locale.getDisplayLanguage()));
        return stories;

        @RequestMapping("/user/{id}")
                public String searchForUser(@PathVariable(value = "id")String id)throw  Exception{
            if(id = null)
                throw new Exception("Nincs ilyen user");
            return user;
        }
        @ExceptionHandler(Exception.class)
        public String exceptionHanndler(HttpServletRequest rA, Exception ex, Model model) {
            model.addAttribute("errMessage", ex.getMessage());
            return "exceptionHandler";
        }
        private ArrayList<Story> getStories(){
            ArrayList<Story> stories = new ArrayList<>();
            return stories;
        }

    }

}
