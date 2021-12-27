package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.Animal;
import agh.ics.oop.Grass;
import agh.ics.oop.Engine;
import agh.ics.oop.Vector2d;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static java.lang.System.exit;

public class App extends Application {
    private AbstractWorldMap firstMap;
    private GridPane firstMapGrid;
    private Label firstGenotype;
    private Animal firstAnimal = null;
    private Label firstAnimalLabel;
    private boolean firstDominantGenotypeFlag = false;
    private Engine engine1;

    private AbstractWorldMap secondMap;
    private GridPane secondMapGrid;
    private Label secondGenotype;
    private Animal secondAnimal = null;
    private Label secondAnimalLabel;
    private boolean secondDominantGenotypeFlag = false;
    private Engine engine2;

    private Stage primaryStage;
    private XYChart<Number, Number> lineChart1;
    private XYChart.Series series11;
    private XYChart.Series series12;
    private XYChart<Number, Number> lineChart2;
    private XYChart.Series series21;
    private XYChart.Series series22;
    private XYChart<Number, Number> lineChart3;
    private XYChart.Series series31;
    private XYChart<Number, Number> lineChart5;
    private XYChart.Series series51;
    private XYChart<Number, Number> lineChart7;
    private XYChart.Series series71;
    private XYChart<Number, Number> lineChart4;
    private XYChart.Series series41;
    private XYChart<Number, Number> lineChart6;
    private XYChart.Series series61;
    private XYChart<Number, Number> lineChart8;
    private XYChart.Series series81;

    private final File csvFile = new File("changelog.csv");
    private final PrintWriter out = new PrintWriter(csvFile);
    private long dunnoWhatIAmDoingButItWorks = -1;
    private long dunnoWhatIAmDoingButItWorks2 = -1;

    public App() throws FileNotFoundException {
    }

    public void pseudoInit(int width, int height, int jungleRation, int startAnimals, int grassValue, int startEnergy, int cost) {
        firstMap = new AbstractWorldMap(width, height, jungleRation, startAnimals, grassValue, (int)Math.floor((float)startEnergy / 2), startEnergy, cost, false);
        engine1 = new Engine(firstMap);
        engine1.addAppObserver(this);

        secondMap = new AbstractWorldMap(width, height, jungleRation, startAnimals, grassValue, (int)Math.floor((float)startEnergy / 2), startEnergy, cost, true);
        engine2 = new Engine(secondMap);
        engine2.addAppObserver(this);

        NumberAxis xAxis1 = new NumberAxis();
        NumberAxis yAxis1 = new NumberAxis();
        xAxis1.setLabel("Age");
        lineChart1 = new LineChart<>(xAxis1,yAxis1);
        series11 = new XYChart.Series();
        series11.setName("Grasses");
        series12 = new XYChart.Series();
        series12.setName("Animals");
        lineChart1.getData().addAll(series11, series12);
        lineChart1.setMaxWidth(430);
        lineChart1.setMaxHeight(400);

        xAxis1 = new NumberAxis();
        yAxis1 = new NumberAxis();
        xAxis1.setLabel("Age");
        lineChart2 = new LineChart<>(xAxis1,yAxis1);
        series21 = new XYChart.Series();
        series21.setName("Grasses");
        series22 = new XYChart.Series();
        series22.setName("Animals");
        lineChart2.getData().addAll(series21, series22);
        lineChart2.setMaxWidth(430);
        lineChart2.setMaxHeight(400);

        xAxis1 = new NumberAxis();
        yAxis1 = new NumberAxis();
        xAxis1.setLabel("Age");
        lineChart3 = new LineChart<>(xAxis1,yAxis1);
        series31 = new XYChart.Series();
        series31.setName("Avg energy (living)");
        lineChart3.getData().addAll(series31);
        lineChart3.setMaxWidth(430);
        lineChart3.setMaxHeight(400);

        xAxis1 = new NumberAxis();
        yAxis1 = new NumberAxis();
        xAxis1.setLabel("Age");
        lineChart4 = new LineChart<>(xAxis1,yAxis1);
        series41 = new XYChart.Series();
        series41.setName("Avg energy (living)");
        lineChart4.getData().addAll(series41);
        lineChart4.setMaxWidth(430);
        lineChart4.setMaxHeight(400);

        xAxis1 = new NumberAxis();
        yAxis1 = new NumberAxis();
        xAxis1.setLabel("Age");
        lineChart5 = new LineChart<>(xAxis1,yAxis1);
        series51 = new XYChart.Series();
        series51.setName("Avg lifetime (dead)");
        lineChart5.getData().addAll(series51);
        lineChart5.setMaxWidth(430);
        lineChart5.setMaxHeight(400);

        xAxis1 = new NumberAxis();
        yAxis1 = new NumberAxis();
        xAxis1.setLabel("Age");
        lineChart6 = new LineChart<>(xAxis1,yAxis1);
        series61 = new XYChart.Series();
        series61.setName("Avg lifetime (dead)");
        lineChart6.getData().addAll(series61);
        lineChart6.setMaxWidth(430);
        lineChart6.setMaxHeight(400);

        xAxis1 = new NumberAxis();
        yAxis1 = new NumberAxis();
        xAxis1.setLabel("Age");
        lineChart7 = new LineChart<>(xAxis1,yAxis1);
        series71 = new XYChart.Series();
        series71.setName("Avg No Children (living)");
        lineChart7.getData().addAll(series71);
        lineChart7.setMaxWidth(430);
        lineChart7.setMaxHeight(400);

        xAxis1 = new NumberAxis();
        yAxis1 = new NumberAxis();
        xAxis1.setLabel("Age");
        lineChart8 = new LineChart<>(xAxis1,yAxis1);
        series81 = new XYChart.Series();
        series81.setName("Avg No Children (living)");
        lineChart8.getData().addAll(series81);
        lineChart8.setMaxWidth(430);
        lineChart8.setMaxHeight(400);

        out.println("---DANE Z OSTATNIEJ SYMULACJI---");

        Thread thread1 = new Thread(engine1);
        Thread thread2 = new Thread(engine2);
        thread1.start();
        thread2.start();
    }

    public void start(Stage primaryStage){
        Label info = new Label("""
                --INFO--
                teoretycznie mapa o powierzchni 3000 poszlaby na tej symulacji jednak z powodow wydajnosciowych dla map o wprowadzonej powierzchni
                powyzej 2500 symulacja nie odpali sie (RunTimeException -> exit(0)), osobisci npolecam mapy o powierzchni do 2000 coby laptop za bardzo nie chcial odleciec
                
                Dodatkowe informacje:
                -lewa mapa to ta zawijana, prawa ogrodzona murem
                -trawa oznaczona jest zielonymi prostokatami, zgnilozielony jungla, zielony step
                -zwierzeta sa czerwonymi kolkami, im bardziej wyblakle tym ma mniej energii
                -osobniki z wiodacym genotypem podswietla sie na niebiesko
                -mapa jest takiej wielkosci jak jej czarny obrys
                -"turn off and save the data" bezpowrotnie zamknie symulacje (ale nie okno), jest to jedyny one way button
                -jesli wprowadzone liczby beda wybiegaly poza dane granice to zostana do nich dostosowane
                -wprowadzenie czegos innego niz liczby zamknie program (RunTimeException -> exit(0))
                -symulacja na danej mapie zakonczy sie po wcisnieciu "turn off and save the data" LUB gdy na mapie zostanie tylko jedno zwierzatko, aby oszczedzic mu zycia w samotnosci
                -informacje o stanie map w poszczegolnych epokach nie zapisza sie niestety bez klikniecia "turn off and save the data"
                -w pliku zapisuja sie dane tylko z ostatniej symulacji ze wzgledu na ilosc tresci jaka generuje symulacja (1 minuta -> kilka tysiecy linii tekstu)
                _______________________________________________________________________________________________
                
                """);
        Label w = new Label("szerokosc: (min:4   max:100)");
        TextField wText = new TextField();
        Label h = new Label("wysokosc: (min:4   max:100)");
        TextField hText = new TextField();
        Label a = new Label("poczatkowa ilosc zwierzatek: (min:2   max:30)");
        TextField aText = new TextField();
        Label j = new Label("stosunek dzungli do stepu: (w punktach procentowych); (min:10)");
        TextField jText = new TextField();
        Label g = new Label("wartosc energetyczna trawy: (min:1)");
        TextField gText = new TextField();
        Label s = new Label("poczatkowa energia pierwszych zwierzatek: (min:1)");
        TextField sText = new TextField();
        Label k = new Label("energia potrzebna na ruch zwierzatka: (min:1)");
        TextField kText = new TextField();
        Button submit = new Button("Submit");
        VBox wrapper = new VBox();
        wrapper.setPadding(new Insets(5,5,5,5));
        wrapper.getChildren().addAll(info, w, wText, h, hText, a, aText, j, jText, g, gText, s, sText, k, kText, submit);
        Scene openScene = new Scene(wrapper, 1000, 700);
        submit.setOnAction(event -> {
            try{
                if(Math.min(Math.max(Integer.parseInt(wText.getText()), 4), 100) * Math.min(Math.max(Integer.parseInt(hText.getText()), 4), 100) > 2500)
                    throw new RuntimeException("map is too big");
                pseudoInit(Math.min(Math.max(Integer.parseInt(wText.getText()), 4), 100), Math.min(Math.max(Integer.parseInt(hText.getText()), 4), 100),
                        Math.max(Integer.parseInt(jText.getText()), 10), Math.min(Math.max(Integer.parseInt(aText.getText()), 2), 30),
                        Math.max(Integer.parseInt(gText.getText()), 1),
                        Math.max(Integer.parseInt(sText.getText()), 1), Math.max(Integer.parseInt(kText.getText()), 1));
            }
            catch(Exception e){
                System.out.println("Exception " + e.getMessage());
                exit(0);
            }
            primaryStage.setScene(prepareScene());
        });

        this.primaryStage = primaryStage;
        primaryStage.show();

        primaryStage.setScene(openScene);
        primaryStage.show();
    }

    public void update(){
        Platform.runLater(() -> {
            firstMapGrid.getChildren().clear();
            secondMapGrid.getChildren().clear();

            gridFill(firstMap.getWidth(), firstMap.getHeight(), firstMapGrid, firstMap, engine1, firstDominantGenotypeFlag);
            gridFill(secondMap.getWidth(), secondMap.getHeight(), secondMapGrid, secondMap, engine2, secondDominantGenotypeFlag);

            firstGenotype.setText("Dominujacy genotyp:\n" + firstMap.getDominantGenotype());
            secondGenotype.setText("Dominujacy genotyp:\n" + secondMap.getDominantGenotype());
            if(firstAnimal != null){
                if(firstAnimal.getEnergy() == -100)
                    firstAnimalLabel.setText("dzieci: " + firstAnimal.getNumberOfChildren() + "\n" + "potomkowie: " + firstAnimal.getNumberOfCousins() + "\n" + "[*]: " + firstAnimal.getAgeOfDeath());
                else
                    firstAnimalLabel.setText("dzieci: " + firstAnimal.getNumberOfChildren() + "\n" + "potomkowie: " + firstAnimal.getNumberOfCousins());
            }
            if(secondAnimal != null){
                if(secondAnimal.getEnergy() == -100)
                    secondAnimalLabel.setText("dzieci: " + secondAnimal.getNumberOfChildren() + "\n" + "potomkowie: " + secondAnimal.getNumberOfCousins() + "\n" + "[*]: " + secondAnimal.getAgeOfDeath());
                else
                    secondAnimalLabel.setText("dzieci: " + secondAnimal.getNumberOfChildren() + "\n" + "potomkowie: " + secondAnimal.getNumberOfCousins());
            }

            series11.getData().add(new XYChart.Data(engine1.getAge(), firstMap.getNumberOfGrass()));
            series21.getData().add(new XYChart.Data(engine2.getAge(), secondMap.getNumberOfGrass()));
            series12.getData().add(new XYChart.Data(engine1.getAge(), firstMap.getNumberOfAnimals()));
            series22.getData().add(new XYChart.Data(engine2.getAge(), secondMap.getNumberOfAnimals()));

            series31.getData().add(new XYChart.Data(engine1.getAge(), engine1.getAvgEnergy()));
            series41.getData().add(new XYChart.Data(engine2.getAge(), engine2.getAvgEnergy()));
            series51.getData().add(new XYChart.Data(engine1.getAge(), engine1.getAvgLifetime()));
            series61.getData().add(new XYChart.Data(engine2.getAge(), engine2.getAvgLifetime()));
            series71.getData().add(new XYChart.Data(engine1.getAge(), engine1.getAvgChildren()));
            series81.getData().add(new XYChart.Data(engine2.getAge(), engine2.getAvgChildren()));

            if(engine1.getAge() > dunnoWhatIAmDoingButItWorks) {
                dunnoWhatIAmDoingButItWorks = engine1.getAge();
                this.out.println("MAP1\nage: " + engine1.getAge() + "\nNoGrass: " + firstMap.getNumberOfGrass() + "\nNoAnimals: " + firstMap.getNumberOfAnimals() +
                        "\nAvgEnergy: " + engine1.getAvgEnergy() + "\nAvgLifetime: " + engine1.getAvgLifetime() + "\nAvgChildren: " + engine1.getAvgChildren() + "\n");
            }
            if(engine2.getAge() > dunnoWhatIAmDoingButItWorks2){
                dunnoWhatIAmDoingButItWorks2 = engine2.getAge();
                this.out.println("MAP2\nage: " + engine2.getAge() + "\nNoGrass: " + secondMap.getNumberOfGrass() + "\nNoAnimals: " + secondMap.getNumberOfAnimals() +
                        "\nAvgEnergy: " + engine2.getAvgEnergy() + "\nAvgLifetime: " + engine2.getAvgLifetime() + "\nAvgChildren: " + engine2.getAvgChildren() + "\n");
            }

            primaryStage.show();

        });
    }

    public void gridFill(int width, int height, GridPane grid, AbstractWorldMap map, Engine engine, boolean flag){
        for(int i=0; i < width; i++){
            for(int j=1; j <= height; j++) {
                Vector2d vector = new Vector2d(i , height-j);
                Object object = map.objectAt(vector);
                if(object instanceof Animal) {
                    double opacity = 1.0;
                    if(((Animal) object).getEnergy() < 0.5 * ((Animal) object).getStartEnergy())
                        opacity = 0.25;
                    else if (((Animal) object).getEnergy() < ((Animal) object).getStartEnergy())
                        opacity = 0.5;

                    Color animalColor;
                    if(flag) {
                        if (((Animal) object).getGenotype().toString().equals(map.getDominantGenotype()))
                            animalColor = new Color(0, 0, 1, 1);
                        else
                            animalColor = new Color(1, 0, 0, opacity);
                    }
                    else
                        animalColor = new Color(1, 0, 0, opacity);
                    Circle circle = new Circle((float)200 / Math.max(map.getWidth(), map.getHeight()), animalColor);
                    circle.setOnMouseClicked(event -> {
                        if(engine.isStopped()){
                            if(map == firstMap)
                                firstAnimal = map.getStrongest(vector).get(0);
                            else
                                secondAnimal = map.getStrongest(vector).get(0);
                        }
                    });
                    grid.add(circle, i, j - 1);
                    GridPane.setHalignment(circle, HPos.CENTER);
                }else if(object instanceof Grass){
                    Color color;
                    if(map.isJungle(vector))
                        color = new Color(0.8, 1, 0, 0.7);
                    else
                        color = new Color(0, 1, 0.6, 0.7);
                    Rectangle square = new Rectangle((float)400 / map.getWidth(), (float)400 / map.getHeight(), color);
                    grid.add(square, i, j - 1);
                    GridPane.setHalignment(square, HPos.CENTER);
                }
                else{
                    Label label = new Label("");
                    grid.add(label, i, height-j);
                    GridPane.setHalignment(label, HPos.CENTER);
                }
            }
        }
    }

    private Scene prepareScene(){
        VBox vbox = new VBox();
        HBox buttons = new HBox(20);
        buttons.setPadding(new Insets(5,5,5,5));
        HBox hbox = new HBox(50);
        hbox.setPadding(new Insets(5,5,5,5));
        HBox genotypes = new HBox(70);
        genotypes.setPadding(new Insets(5,5,5,5));
        HBox animals = new HBox(400);
        animals.setPadding(new Insets(5,5,5,5));

        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        HBox hbox4 = new HBox();
        HBox hbox5 = new HBox();
        hbox.setSpacing(50.0);
        hbox.setPadding(new Insets(5,5,5,5));
        //----------------------------------------------------------------------------------
        firstMapGrid = new GridPane();
        firstMapGrid.setStyle("-fx-border-color: black;");
        firstMapGrid.setMaxHeight(400);
        int width = firstMap.getWidth();
        int height = firstMap.getHeight();
        for(int i=0; i < width; i++) {
            firstMapGrid.getColumnConstraints().add(new ColumnConstraints((float)400 / firstMap.getWidth()));
        }
        for(int i=0; i < height; i++) {
            firstMapGrid.getRowConstraints().add(new RowConstraints((float)400 / firstMap.getHeight()));
        }
        gridFill(width, height, firstMapGrid, firstMap, engine1, firstDominantGenotypeFlag);
        //----------------------------------------------------------------------------------
        secondMapGrid = new GridPane();
        secondMapGrid.setStyle("-fx-border-color: black;");
        secondMapGrid.setMaxHeight(400);
        width = secondMap.getWidth();
        height = secondMap.getHeight();
        for(int i=0; i < width; i++) {
            secondMapGrid.getColumnConstraints().add(new ColumnConstraints((float)400 / secondMap.getWidth()));
        }
        for(int i=0; i < height; i++) {
            secondMapGrid.getRowConstraints().add(new RowConstraints((float)400 / secondMap.getHeight()));
        }
        gridFill(width, height, secondMapGrid, secondMap, engine2, secondDominantGenotypeFlag);
        //----------------------------------------------------------------------------------
        firstGenotype = new Label("Dominujacy genotyp: ");
        secondGenotype = new Label("Dominujacy genotyp: ");
        firstAnimalLabel = new Label("nie wybrano zwierzecia");
        secondAnimalLabel = new Label("nie wybrano zwierzecia");
        //----------------------------------------------------------------------------------
        Button start1 = new Button("start / stop (Map 1)");
        start1.setOnAction(event -> {
            firstDominantGenotypeFlag = false;
            engine1.stop();
        });
        Button start2 = new Button("start / stop (Map 2)");
        start2.setOnAction(event -> {
            secondDominantGenotypeFlag = false;
            engine2.stop();
        });
        Button start12 = new Button("start / stop (Both)");
        start12.setOnAction(event -> {
            firstDominantGenotypeFlag = false;
            secondDominantGenotypeFlag = false;
            engine1.stop();
            engine2.stop();
        });
        Button show1 = new Button("zwierzeta z dominujacym genomem\n (Map 1; musi byc zatrzymana)");
        show1.setOnAction(event -> {
            if(engine1.isStopped()) {
                firstDominantGenotypeFlag = true;
                gridFill(firstMap.getWidth(), firstMap.getHeight(), firstMapGrid, firstMap, engine1, firstDominantGenotypeFlag);
            }
        });
        Button show2 = new Button("zwierzeta z dominujacym genomem\n (Map 2; musi byc zatrzymana)");
        show2.setOnAction(event -> {
            if(engine2.isStopped()) {
                secondDominantGenotypeFlag = true;
                gridFill(secondMap.getWidth(), secondMap.getHeight(), secondMapGrid, secondMap, engine2, secondDominantGenotypeFlag);
            }
        });
        Button logout = new Button("turn Off and save the data");
        logout.setOnAction(event -> {
            this.out.close();
            engine1.end();
            engine2.end();
        });
        buttons.getChildren().addAll(start1, start2, start12, logout);
        genotypes.getChildren().addAll(firstGenotype, secondGenotype);
        animals.getChildren().addAll(firstAnimalLabel, secondAnimalLabel);
        hbox.getChildren().addAll(firstMapGrid, secondMapGrid);
        hbox2.getChildren().addAll(lineChart1, lineChart2);
        hbox3.getChildren().addAll(lineChart3, lineChart4);
        hbox4.getChildren().addAll(lineChart5, lineChart6);
        hbox5.getChildren().addAll(lineChart7, lineChart8);
        vbox.getChildren().addAll(buttons, show1, show2, hbox, genotypes, animals, hbox2, hbox3, hbox4, hbox5);
        ScrollPane scrollPane = new ScrollPane(vbox);
        return new Scene(scrollPane, 1000, 600);
    }

}
