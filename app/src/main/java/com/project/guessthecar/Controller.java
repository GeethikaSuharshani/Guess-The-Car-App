package com.project.guessthecar;

import android.app.Application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Controller extends Application {
    private List<String> carMakes = new ArrayList<>(Arrays.asList("Audi", "BMW", "Bugatti", "Ferrari", "Ford", "Porsche")); //ArrayList that stores car make names in the application
    private List<String> audiCars = new ArrayList<>(Arrays.asList("audi_image1", "audi_image2", "audi_image3", "audi_image4", "audi_image5", "audi_image6", "audi_image7", "audi_image8", "audi_image9", "audi_image10")); //ArrayList that stores audi car images in the application
    private List<String> bmwCars = new ArrayList<>(Arrays.asList("bmw_image1", "bmw_image2", "bmw_image3", "bmw_image4", "bmw_image5", "bmw_image6", "bmw_image7", "bmw_image8", "bmw_image9", "bmw_image10")); //ArrayList that stores bmw car images in the application
    private List<String> bugattiCars = new ArrayList<>(Arrays.asList("bugatti_image1", "bugatti_image2", "bugatti_image3", "bugatti_image4", "bugatti_image5", "bugatti_image6", "bugatti_image7", "bugatti_image8", "bugatti_image9", "bugatti_image10")); //ArrayList that stores bugatti car images in the application
    private List<String> ferrariCars = new ArrayList<>(Arrays.asList("ferrari_image1", "ferrari_image2", "ferrari_image3", "ferrari_image4", "ferrari_image5", "ferrari_image6", "ferrari_image7", "ferrari_image8", "ferrari_image9", "ferrari_image10")); //ArrayList that stores ferrari car images in the application
    private List<String> fordCars = new ArrayList<>(Arrays.asList("ford_image1", "ford_image2", "ford_image3", "ford_image4", "ford_image5", "ford_image6", "ford_image7", "ford_image8", "ford_image9", "ford_image10")); //ArrayList that stores ford car images in the application
    private List<String> porscheCars = new ArrayList<>(Arrays.asList("porsche_image1", "porsche_image2", "porsche_image3", "porsche_image4", "porsche_image5", "porsche_image6", "porsche_image7", "porsche_image8", "porsche_image9", "porsche_image10")); //ArrayList that stores porsche car images in the application

    public List<String> getCarMakes() { //return carMakes list
        return carMakes;
    }

    public void setCarMakes(List<String> carMakes) { //set values for carMakes list
        this.carMakes = carMakes;
    }

    public List<String> getAudiCars() { //return audiCars list
        return audiCars;
    }

    public void setAudiCars(List<String> audiCars) { //set values for audiCars list
        this.audiCars = audiCars;
    }

    public List<String> getBmwCars() { //return bmwCars list
        return bmwCars;
    }

    public void setBmwCars(List<String> bmwCars) { //set values for bmwCars list
        this.bmwCars = bmwCars;
    }

    public List<String> getBugattiCars() { //return bugattiCars list
        return bugattiCars;
    }

    public void setBugattiCars(List<String> bugattiCars) { //set values for bugattiCars list
        this.bugattiCars = bugattiCars;
    }

    public List<String> getFerrariCars() { //return ferrariCars list
        return ferrariCars;
    }

    public void setFerrariCars(List<String> ferrariCars) { //set values for ferrariCars list
        this.ferrariCars = ferrariCars;
    }

    public List<String> getFordCars() { //return fordCars list
        return fordCars;
    }

    public void setFordCars(List<String> fordCars) { //set values for fordCars list
        this.fordCars = fordCars;
    }

    public List<String> getPorscheCars() { //return porscheCars list
        return porscheCars;
    }

    public void setPorscheCars(List<String> porscheCars) { //set values for porscheCars list
        this.porscheCars = porscheCars;
    }

    public List<String> getRandomCarImage() { //return a random image selected from the car images in the application
        Random randomNo = new Random();

        int carMakeIndex;
        String carMake = "";
        if (!carMakes.isEmpty()) {
            carMakeIndex = randomNo.nextInt(carMakes.size());
            carMake = carMakes.get(carMakeIndex); //select a random car make which is available in the application
        }

        int carImageIndex;
        String carImage = "";
        switch (carMake) { //select a random car image according to the selected car make
            case "Audi":
                if (!audiCars.isEmpty()) {
                    carImageIndex = randomNo.nextInt(audiCars.size());
                    carImage = audiCars.get(carImageIndex);
                }
                break;
            case "BMW":
                if (!bmwCars.isEmpty()) {
                    carImageIndex = randomNo.nextInt(bmwCars.size());
                    carImage = bmwCars.get(carImageIndex);
                }
                break;
            case "Bugatti":
                if (!bugattiCars.isEmpty()) {
                    carImageIndex = randomNo.nextInt(bugattiCars.size());
                    carImage = bugattiCars.get(carImageIndex);
                }
                break;
            case "Ferrari":
                if (!ferrariCars.isEmpty()) {
                    carImageIndex = randomNo.nextInt(ferrariCars.size());
                    carImage = ferrariCars.get(carImageIndex);
                }
                break;
            case "Ford":
                if (!fordCars.isEmpty()) {
                    carImageIndex = randomNo.nextInt(fordCars.size());
                    carImage = fordCars.get(carImageIndex);
                }
                break;
            case "Porsche":
                if (!porscheCars.isEmpty()) {
                    carImageIndex = randomNo.nextInt(porscheCars.size());
                    carImage = porscheCars.get(carImageIndex);
                }
                break;
        }
        return new ArrayList<>(Arrays.asList(carImage,carMake)); //return an ArrayList with the details of the selected car image and the car make of that image
    }

}
