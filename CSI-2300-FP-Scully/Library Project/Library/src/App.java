import java.util.LinkedList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;


public class App extends Application{
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    //Book class
    public class Book {
    
        private String title;
        private String authorLName;
        private String authorFName;
        private boolean checkedOut = false;
        private User currentHolder;// to keep track of who has what
        private String genre;
        private double ddn = -1; //(dewey decimal number) neg one by default to initialize it

        public Book(){
        
        }
        public Book(String title, String authorLName, String authorFName){
            this.title = title;
            this.authorFName = authorFName;
            this.authorLName = authorLName;
        }

        
        public String getTitle(){
            return this.title;
        }
        public String getAuthorLastName(){
            return this.authorLName;
        }
        public String getAuthorFirstName(){
            return this.authorFName;
        }
        public boolean getCheckedStatus(){
            return this.checkedOut;
        }
        public User currentHolder(){
            return this.currentHolder;
        }
        public void setHolder(User currentHolder){
            this.currentHolder = currentHolder;
        }
        public void changeChecked(){
            if(this.checkedOut == false)
                checkedOut = true;
            else
                checkedOut = false;
        }
        public void setGenre(String genre){
            this.genre = genre;
        }
        public String getGenre(){
            return this.genre;
        }
        public void setDDN(Double ddn){
            this.ddn = ddn;
        }
        public double getDDN(){
            return this.ddn;
        }
    }
    
    public class User{
        private String userLastName;
        private String userFirstName;
        private int idNum;
        LinkedList checkedOutBooks;

        public User(){
            checkedOutBooks = new LinkedList<>();
        }
        public User(String fName, String lName, int id){
            this.userFirstName = fName;
            this.userLastName =lName;
            this.idNum = id;
            checkedOutBooks = new LinkedList<>();
        }
        public String getLName(){
            return this.userLastName;
        }
        public String getFName(){
            return this.userFirstName;
        }
        public int getID(){
            return this.idNum;
        }
        public LinkedList<Book> getBooks(){
            return this.checkedOutBooks;
        }
    }

    public LinkedList<Book> catalogue = new LinkedList<Book>();
    public LinkedList<User> userBase = new LinkedList<User>(); 
    private TextFlow displayTextFlow = new TextFlow();
    private String selectedListView, lastSelectedSorting;
    private Book selectedBook;
    TextField ficTextField, nonFTextField;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Establish entire Pane
        Pane pane = new Pane();
        pane.setPrefSize(800, 500);

        
        displayTextFlow.setPrefSize(100, 300);
        displayTextFlow.setStyle("-fx-background-color: darkgray;");
        displayTextFlow.getChildren().addAll(new Text("\t\t\tINFO\n"));

        //creates catalogue list
        VBox listButtonBox = new VBox(10);
        Insets lbbpadding = new Insets(0,0,0,10);
        listButtonBox.setPadding(lbbpadding);
        //Creates list
        VBox listBox = new VBox(10);
        Label listTitle = new Label("Catalogue");
        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList();
        list.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() >= 0) {
                displayBookInfo(items.get(newVal.intValue()));
                selectedListView = items.get(newVal.intValue());
                System.out.println(selectedListView);
            }});
        list.setItems(items);
        list.setPrefSize(400, 350);
        listBox.getChildren().addAll(listTitle, list);
        

        //Creates buttons
        HBox buttonBox = new HBox(60);
        VBox cOutBox = new VBox(10);
        Label cOutLabel = new Label("Check Out a Book");
        Button cOut = new Button();
        cOut.setPrefSize(60, 40);
        cOutBox.getChildren().addAll(cOutLabel, cOut);
        
        VBox cInBox = new VBox(10);
        Label cInLabel = new Label("Check In a Book");
        Button cIn = new Button();
        cInBox.getChildren().addAll(cInLabel, cIn);
        cIn.setPrefSize(60, 40);
        
        VBox addBox = new VBox(10);
        Label addLabel = new Label("Add a Book");
        Button add = new Button();
        addBox.getChildren().addAll(addLabel, add);
        add.setPrefSize(60, 40);
        
        VBox removeBox = new VBox(10);
        Label removeLabel = new Label("Remove a Book");
        Button remove = new Button();
        removeBox.getChildren().addAll(removeLabel, remove);
        remove.setPrefSize(60, 40);
        
        buttonBox.getChildren().addAll(addBox, removeBox, cOutBox, cInBox);

        listButtonBox.getChildren().addAll(listBox, buttonBox);

    //to organize the two halfs of the display
        HBox sidebysideBox = new HBox(40);
        VBox rightBox = new VBox(50);
        
        VBox sortDisplayBox = new VBox(5);
        //Combobox for the sorting options (the drop down list)
        Label sortingLabel = new Label("Sorting Options");
        String sortingOptions[] = {"Alphabetical", "Descending Alphabetical", "Author's Name Alphabetical", "Author's Name D. Alphabetical"};
        ComboBox<String> sortingList = new ComboBox<>(FXCollections.observableArrayList(sortingOptions));
        sortingList.setOnAction(event -> {
            String selectedSorting = sortingList.getValue();
            lastSelectedSorting = selectedSorting;
            sortMe(selectedSorting, items);
        });
        sortDisplayBox.getChildren().addAll(sortingLabel, sortingList);
        

       

        rightBox.getChildren().addAll(sortDisplayBox, displayTextFlow);

        


        //ADD A BOOK BUTTON FUNCTION
        add.setOnMouseClicked(event -> {
           Stage textFieldStage = new Stage();
           VBox addStageVBox = new VBox(20);
           HBox bookNamePromptBox = new HBox(10);
           bookNamePromptBox.setPadding(new Insets(5,5,5,0));
            
           Label bookNamePromptLabel = new Label("Book Title:");
           bookNamePromptLabel.setPadding(new Insets(5,5,5,10));
           TextField nameTextField = new TextField();
           nameTextField.setPrefSize(400, 10);
           bookNamePromptBox.getChildren().addAll(/*addStageTitleLabel,*/ bookNamePromptLabel, nameTextField);
            
           HBox authorLNamePromptBox = new HBox(10);
           authorLNamePromptBox.setPadding(new Insets(5,5,5,0));
           Label authorLNamePromptLabel = new Label("Author Last Name:");
           authorLNamePromptLabel.setPadding(new Insets(5,5,5,10));
           TextField authorLNameTextField = new TextField();
           authorLNameTextField.setPrefSize(350, 10);
           authorLNamePromptBox.getChildren().addAll(authorLNamePromptLabel, authorLNameTextField);
            
           HBox authorFNamePromptBox = new HBox(10);
           authorFNamePromptBox.setPadding(new Insets(5,5,5,0));
           Label authorFNamePromptLabel = new Label("Author First Name:");
           authorFNamePromptLabel.setPadding(new Insets(5,5,5,10));
           TextField authorFNameTextField = new TextField();
           authorFNameTextField.setPrefSize(350, 10);
           authorFNamePromptBox.getChildren().addAll(authorFNamePromptLabel, authorFNameTextField);
            
           HBox checkBoxBox = new HBox();
           ToggleGroup toggleGroup = new ToggleGroup();
           HBox fCheckBoxBox = new HBox();
           Label fCheckBoxLabel = new Label("Fiction: ");
            RadioButton fCheckBox = new RadioButton();
            fCheckBoxBox.setPadding(new Insets(0,70,0,30));
            fCheckBoxBox.getChildren().addAll(fCheckBoxLabel, fCheckBox);
            
            HBox nonCheckBoxBox = new HBox();
            Label nonFCheckBoxLabel = new Label("Non-fiction: ");
            RadioButton nonFCheckBox = new RadioButton();
            nonCheckBoxBox.setPadding(new Insets(0,30,0,70));
            nonCheckBoxBox.getChildren().addAll(nonFCheckBoxLabel, nonFCheckBox);
            
            checkBoxBox.setPadding(new Insets(0,70,0,70));
            fCheckBox.setToggleGroup(toggleGroup);
            nonFCheckBox.setToggleGroup(toggleGroup);
            checkBoxBox.getChildren().addAll(fCheckBoxBox, nonCheckBoxBox);

        

  
            HBox addStagebuttonBox = new HBox();
            Button submitAddButton = new Button();
            addStagebuttonBox.setPadding(new Insets(5,220,5,220));
            submitAddButton.setText("Submit");
            submitAddButton.setOnMouseClicked(subEvent -> {
                Book newBook = new Book(nameTextField.getText(), authorLNameTextField.getText(), authorFNameTextField.getText());
                catalogue.add(newBook);
                list.getItems().add(nameTextField.getText());
                if(fCheckBox.isSelected())
                    newBook.setGenre(ficTextField.getText());
                else if(nonFCheckBox.isSelected())
                    newBook.setDDN(Double.parseDouble(nonFTextField.getText()));
                textFieldStage.close();
                sortMe(lastSelectedSorting, items);
                printCatalogue();
            });
            addStagebuttonBox.getChildren().addAll(submitAddButton);


           addStageVBox.getChildren().addAll(bookNamePromptBox, authorFNamePromptBox, authorLNamePromptBox, checkBoxBox, addStagebuttonBox);
           Scene newScene = new Scene(addStageVBox, 500, 300);
           textFieldStage.setTitle("Add a book");
           textFieldStage.setScene(newScene);
           textFieldStage.show();

            fCheckBox.setOnAction(checkEvent -> {
                addStageVBox.getChildren().clear();
                addStageVBox.getChildren().addAll(bookNamePromptBox, authorFNamePromptBox, authorLNamePromptBox, checkBoxBox, checkBoxSelected(fCheckBox, nonFCheckBox), addStagebuttonBox);
            });
            nonFCheckBox.setOnAction(checkEvent2 -> {
                addStageVBox.getChildren().clear();
                addStageVBox.getChildren().addAll(bookNamePromptBox, authorFNamePromptBox, authorLNamePromptBox, checkBoxBox, checkBoxSelected(fCheckBox, nonFCheckBox), addStagebuttonBox);
            });
        });

        //REMOVE BUTTON FUNCTION
        remove.setOnMouseClicked(event2  -> {
            Book selectedBook = findBookByTitle(selectedListView);
            
            list.getItems().remove(selectedBook.getTitle());
            //sets selected to next item on list when removed (if there is another)
            int selectedIndex = items.indexOf(selectedListView);
            if (selectedIndex != -1 && selectedIndex < items.size() - 1) {
                selectedListView = items.get(selectedIndex + 1);
            }

            
            displayTextFlow.getChildren().clear();
            displayTextFlow.getChildren().addAll(new Text("\t\t\tINFO\n"));
            // for (Book book : catalogue) {
            //     if (book.getTitle().equals(selectedBook.g)) {
            // }
            catalogue.remove(selectedBook);
            sortMe(lastSelectedSorting, items);
            updateSelectedBook();
            printCatalogue();
        });


        //CHECK OUT BUTTON
        cOut.setOnMouseClicked(event3 -> {
            Stage cOutStage = new Stage();
            VBox cOutStageBox = new VBox();

            HBox firstNamePromptBox = new HBox();
            firstNamePromptBox.setPadding(new Insets(5,5,5,0));
            Label firstNamePromtLabel = new Label("First Name: ");
            firstNamePromtLabel.setPadding(new Insets(5,5,5,5));
            TextField firstNameTextField = new TextField();
            firstNameTextField.setPrefSize(400, 10);
            firstNamePromptBox.getChildren().addAll(firstNamePromtLabel, firstNameTextField);

            HBox lastNamePromptBox = new HBox();
            lastNamePromptBox.setPadding(new Insets(5,5,5,0));
            Label lastNamePromtLabel = new Label("Last Name: ");
            lastNamePromtLabel.setPadding(new Insets(5,5,5,5));
            TextField lastNameTextField = new TextField();
            lastNameTextField.setPrefSize(400, 10);
            lastNamePromptBox.getChildren().addAll(lastNamePromtLabel, lastNameTextField);


            HBox idPromptBox = new HBox();
            idPromptBox.setPadding(new Insets(10,5,5,0));
            Label idPromtLabel = new Label("Library ID Number: ");
            idPromtLabel.setPadding(new Insets(5,5,5,5));
            TextField idTextField = new TextField();
            idTextField.setPrefSize(355, 10);
            idPromptBox.getChildren().addAll(idPromtLabel, idTextField);


            HBox cOutStagebuttonBox = new HBox();
            Button submitCOutButton = new Button();
            cOutStagebuttonBox.setPadding(new Insets(10,220,10,220));
            submitCOutButton.setText("Submit");
            submitCOutButton.setOnMouseClicked(subEvent -> {
                
               
                Book selectedBook = findBookByTitle(selectedListView);
                if(selectedBook.checkedOut == false){
                    if(foundUser(Integer.parseInt(idTextField.getText())) != true){
                        User newUser = new User(firstNameTextField.getText(), lastNameTextField.getText(), Integer.parseInt(idTextField.getText()));
                        userBase.add(newUser);
                        selectedBook.setHolder(newUser);
                        newUser.checkedOutBooks.add(selectedBook);
                        System.out.println(newUser.checkedOutBooks.getLast());
                        printUserbase();
                        updateSelectedBook();
                    }
                    else{
                        User user = findUserByID(Integer.parseInt(idTextField.getText()));
                        user.checkedOutBooks.add(selectedBook);
                        selectedBook.setHolder(user);
                        System.out.println(user.checkedOutBooks.getLast());
                        updateSelectedBook();
                    }
                int selectedIndex = items.indexOf(selectedListView);
                if (selectedIndex != -1 && selectedIndex < items.size() - 1) {
                    selectedListView = items.get(selectedIndex + 1);
                }
                selectedBook.changeChecked();
               
                cOutStage.close();
                }
                else{
                    Text checkedOut = new Text("That book is already checked out.");
                    displayTextFlow.getChildren().clear();
                    displayTextFlow.getChildren().addAll(new Text("\t\t\tINFO\n"), checkedOut);
                }
                displayBookInfo(selectedBook.title);
        
            });
            
           

            cOutStagebuttonBox.getChildren().addAll(submitCOutButton);


            cOutStageBox.getChildren().addAll(firstNamePromptBox, lastNamePromptBox, idPromptBox, cOutStagebuttonBox);
            Scene cOutScene = new Scene(cOutStageBox, 500, 200);
            cOutStage.setTitle("Please enter account information");
            cOutStage.setScene(cOutScene);
            cOutStage.show();
        });


        ///CIN BUTTON
        cIn.setOnMouseClicked(event4 -> {
            System.out.println(selectedListView);
            Book selectedBook = findBookByTitle(selectedListView);
            if(selectedBook.checkedOut == true){
                System.out.println(selectedBook.getTitle());
                User holder = selectedBook.currentHolder();
                selectedBook.currentHolder = null;
                selectedBook.checkedOut = false;
                holder.checkedOutBooks.remove(selectedBook);
                displayBookInfo(selectedBook.title);
                selectedListView = null;
            }

        });



        sidebysideBox.getChildren().addAll(listButtonBox, rightBox);
        pane.getChildren().add(sidebysideBox);
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Library Catalogue");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
//main scene ^ 
//////////////////////////////////////////////////////////
//methods
    private void displayBookInfo(String bookTitle) {
        displayTextFlow.getChildren().clear();
    
        Book selectedBook = findBookByTitle(bookTitle);
        if (selectedBook != null) {
            Text titleText = new Text("Title: " + selectedBook.getTitle() + "\n");
            Text authorText = new Text("Author: " + selectedBook.getAuthorFirstName() + " " + selectedBook.getAuthorLastName() +" \n");
            Text radioButtonText = new Text(" ");
            Text checkedStatus = new Text(" ");
            
            if(selectedBook.getGenre() != null){
                radioButtonText.setText("Genre: " + selectedBook.getGenre() + " \n");
            }
            else if(selectedBook.getDDN() != -1){
                radioButtonText.setText("Dewey Decimal Number: " + selectedBook.getDDN() + " \n");
            }
            if(selectedBook.checkedOut == true){
                checkedStatus = new Text("Checked Out\n");
                Text currentHolder = new Text((selectedBook.currentHolder().getFName() + " " +selectedBook.currentHolder().getLName()) +" \n");
                System.out.println(currentHolder);
                displayTextFlow.getChildren().addAll(new Text("\t\t\tINFO\n"), titleText, authorText, radioButtonText, checkedStatus, currentHolder);
            }
            else{
                checkedStatus = new Text("Available \n");
                displayTextFlow.getChildren().addAll(new Text("\t\t\tINFO\n"), titleText, authorText, radioButtonText, checkedStatus);
            }
        }
        else {
            Text notFoundText = new Text("Error: Book not found");
            displayTextFlow.getChildren().add(notFoundText);
        }
    }

    private Book findBookByTitle(String title) {
        for (Book book : catalogue) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    private User findUserByID(int id){
        for(User user : userBase){
            if(user.getID() == id){
                return user;
            }
        }
        return null;
    }

    private boolean foundUser(int id){
        for(User user : userBase){
            if(user.getID() == id){
                return true;
            }
        }
        return false;
    }

    private void sortMe(String selected, ObservableList<String> items){
        if(selected == "Alphabetical"){
            sortAlpha(items);
        }
        else if(selected == "Descending Alphabetical"){
            sortRAlpha(items);
        }
        else if(selected == "Author's Name Alphabetical"){
            sortAuthorAlpha(items);
        }
        else if(selected == "Author's Name D. Alphabetical"){
            sortAuthorRAlpha(items);
        }
        else{
            sortAlpha(items);
        }
        lastSelectedSorting = selected;
    }
    private void sortAlpha(ObservableList<String> list){
        int x = list.size();
        for (int i = 0; i < x - 1; i++) {
            for (int j = 0; j < x - i - 1; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    String plchldr = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, plchldr);
                }
            }
        }
    }
    private void sortRAlpha(ObservableList<String> list){
        int x = list.size();
        for (int i = 0; i < x - 1; i++) {
            for (int j = 0; j < x - i - 1; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) < 0) {
                    String plchldr = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, plchldr);
                }
            }
        }
    }
    private void sortAuthorAlpha(ObservableList<String> list){
        int x = list.size();
        for (int i = 0; i < x - 1; i++) {
            for (int j = 0; j < x - i - 1; j++) {
                if (findBookByTitle(list.get(j)).getAuthorLastName().compareTo(findBookByTitle(list.get(j + 1)).getAuthorLastName()) > 0) {
                    String plchldr = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, plchldr);
                }
            }
        }
    }
    private void sortAuthorRAlpha(ObservableList<String> list){
        int x = list.size();
        for (int i = 0; i < x - 1; i++) {
            for (int j = 0; j < x - i - 1; j++) {
                if (findBookByTitle(list.get(j)).getAuthorLastName().compareTo(findBookByTitle(list.get(j + 1)).getAuthorLastName()) < 0) {
                    String plchldr = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, plchldr);
                }
            }
        }
    }

    private void updateSelectedBook() {
        selectedBook = findBookByTitle(selectedListView);
    }

    private HBox checkBoxSelected(RadioButton f, RadioButton n){
        HBox returnHBox = new HBox(10);
        if(f.isSelected() == true){
            Label fictLabel = new Label("Genre: ");
            ficTextField = new TextField();
            ficTextField.setPrefSize(420, 10);
            returnHBox.getChildren().addAll(fictLabel, ficTextField);
        }
        else{
          Hyperlink nonfHyperlink = new Hyperlink("Dewey Decimal System Number: ");
          nonfHyperlink.setOnAction(linkevent -> {
            getHostServices().showDocument("https://en.wikipedia.org/wiki/List_of_Dewey_Decimal_classes");
          });
        //   Label nonFLabel = new Label("Dewey Decimal System Number: ");
          nonFTextField = new TextField();
          nonFTextField.setPrefSize(280, 10);
          returnHBox.getChildren().addAll(nonfHyperlink, nonFTextField);
        }
        returnHBox.setPadding(new Insets(0,5,0,10));
        return returnHBox;
    }
////////////////////////////////////////////////////
    //USED TO TEST LINKED LIST INTERNALLY
    private void printCatalogue() {
        for (Book book : catalogue) {
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthorFirstName() + " " + book.getAuthorLastName());
            System.out.println("Checked Out: " + (book.getCheckedStatus() ? "Yes" : "No"));
            System.out.println("");
        }
        System.out.println("/end");
    }
    private void printUserbase() {
        for (User user : userBase) {
            System.out.println("FName: " + user.getFName());
            System.out.println("LName: " + user.getLName());
            System.out.println("ID: " + user.getID());
            System.out.println("");
        }
        System.out.println("/end");
    }
   
}
