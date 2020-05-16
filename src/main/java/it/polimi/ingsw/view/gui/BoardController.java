package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapPlayer;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.clientmessages.*;
import it.polimi.ingsw.commons.servermessages.CheckBuildServer;
import it.polimi.ingsw.commons.servermessages.CheckMoveServer;
import it.polimi.ingsw.commons.servermessages.CurrentStatusServer;
import it.polimi.ingsw.commons.servermessages.QuestionAbilityServer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class BoardController extends DefaultController {

    @FXML
    public Button buttonSelected1;

    @FXML
    public Button buttonSelected2;

    @FXML
    public Button buttonSelected3;

    @FXML
    public Button buttonGod1;

    @FXML
    public Button buttonGod2;

    @FXML
    public Button buttonGod3;

    @FXML
    public Button buttonName1;

    @FXML
    public Button buttonName2;

    @FXML
    public Button buttonName3;

    @FXML
    public Button buttonDescription;

    /**
     * Choose size of the Image for Towers Floors
     */
    int towerSize = 80;

    /**
     * Choose size of the Image for Pawns
     */
    int pawnSize = 40;

    /**
     * state of the buttons to change the ClientMessage generated once clicked
     * 0 = Worker Initialize
     * 1 = Worker Choose
     * 2 = Move
     * 3 = Build
     * 4 = No Action
     */
    private int state;

    CurrentStatusServer Css;
    CheckMoveServer Cms;
    CheckBuildServer Cbs;
    QuestionAbilityServer Qas;

    public void setCss(CurrentStatusServer css) {
        Css = css;
    }
    public void setCms(CheckMoveServer cms) {
        Cms = cms;
    }
    public void setCbs(CheckBuildServer cbs) {
        Cbs = cbs;
    }
    public void setQas(QuestionAbilityServer qas) { Qas = qas; }

    Image floor1 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor1.png", towerSize, towerSize, true, false);
    Image floor2 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor2.png", towerSize, towerSize, true, false);
    Image floor3 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor3.png", towerSize, towerSize, true, false);
    Image dome = new Image("/it.polimi.ingsw/view/gui/img/tower/dome.png", towerSize, towerSize, true, false);


    @FXML
    TextField banner;
    Text workerITA = new Text("Choose the position of the first worker");
    Text workerITA2 = new Text("Choose the position of the second worker");
    Text workerCTA = new Text("Choose a Worker");
    Text moveTA = new Text("Choose the cell where you want to move");
    Text buildTA = new Text("Choose the cell where you want to build");
    Text questionTA = new Text("Do you want to use you God ability?");

    @FXML
    Button yes,no;

    @FXML
    Button cell00, cell01, cell02, cell03, cell04,
            cell10, cell11, cell12, cell13, cell14,
            cell20, cell21, cell22, cell23, cell24,
            cell30, cell31, cell32, cell33, cell34,
            cell40, cell41, cell42, cell43, cell44;

    @FXML
    ImageView block00, block01, block02, block03, block04,
            block10, block11, block12, block13, block14,
            block20, block21, block22, block23, block24,
            block30, block31, block32, block33, block34,
            block40, block41, block42, block43, block44;

    @FXML
    ImageView square00, square01, square02, square03, square04,
            square10, square11, square12, square13, square14,
            square20, square21, square22, square23, square24,
            square30, square31, square32, square33, square34,
            square40, square41, square42, square43, square44;


    @FXML
    @Override
    public void initialize() {
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_match.png"));

        double w = 120;
        buttonSelected1.setPrefSize(w,60);
        buttonSelected2.setPrefSize(w,60);
        buttonSelected3.setPrefSize(w,60);

        double h = 130;
        w = 130;
        buttonGod1.setMinSize(w,h);
        buttonGod1.setMaxSize(w,h);
        buttonGod1.setPrefSize(w,h);
        buttonGod2.setMinSize(w,h);
        buttonGod2.setMaxSize(w,h);
        buttonGod2.setPrefSize(w,h);
        buttonGod3.setMinSize(w,h);
        buttonGod3.setMaxSize(w,h);
        buttonGod3.setPrefSize(w,h);

        h = 70;
        w = 120;
        buttonName3.setMinSize(w,h);
        buttonName3.setMaxSize(w,h);
        buttonName3.setPrefSize(w,h);
        buttonName1.setMinSize(w,h);
        buttonName1.setMaxSize(w,h);
        buttonName1.setPrefSize(w,h);
        buttonName2.setMinSize(w,h);
        buttonName2.setMaxSize(w,h);
        buttonName2.setPrefSize(w,h);

        buttonDescription.setMinSize(375,200);
        buttonDescription.setMaxSize(375,200);
        buttonDescription.setPrefSize(375,200);
    }

    @Override
    public void setup(){
        int start = 550;
        buttonGod1.getStyleClass().clear();
        buttonGod2.getStyleClass().clear();
        buttonGod3.getStyleClass().clear();

        buttonName1.setLayoutY(10);
        buttonName2.setLayoutY(10);
        buttonName3.setLayoutY(10);

        buttonDescription.setLayoutX(start + (gui.sceneWidth-start)/2 - buttonDescription.getPrefWidth()/2);
        buttonDescription.setLayoutY(gui.sceneHeight-bottom.getPrefHeight()-top.getPrefHeight()-buttonDescription.getPrefHeight()-10);

        setDescription("");

        if(gui.getClient().getPlayers().size() == 3){
            int offset = 30;
            if(gui.getClient().getMyPlayer().name.equals(gui.getClient().getPlayers().get(0).name)){
                buttonGod1.getStyleClass().addAll("button",gui.getClient().getPlayers().get(1).card.toString().toLowerCase());
                buttonName1.setText(gui.getClient().getPlayers().get(1).name + "\n" + gui.getClient().getPlayers().get(1).card.toString());
                buttonGod3.getStyleClass().addAll("button",gui.getClient().getPlayers().get(2).card.toString().toLowerCase());
                buttonName3.setText(gui.getClient().getPlayers().get(2).name + "\n" + gui.getClient().getPlayers().get(2).card.toString());
            } else if(gui.getClient().getMyPlayer().name.equals(gui.getClient().getPlayers().get(1).name)){
                buttonGod1.getStyleClass().addAll("button",gui.getClient().getPlayers().get(0).card.toString().toLowerCase());
                buttonName1.setText(gui.getClient().getPlayers().get(0).name + "\n" + gui.getClient().getPlayers().get(0).card.toString());
                buttonGod3.getStyleClass().addAll("button",gui.getClient().getPlayers().get(2).card.toString().toLowerCase());
                buttonName3.setText(gui.getClient().getPlayers().get(2).name + "\n" + gui.getClient().getPlayers().get(2).card.toString());
            } else if(gui.getClient().getMyPlayer().name.equals(gui.getClient().getPlayers().get(2).name)){
                buttonGod1.getStyleClass().addAll("button",gui.getClient().getPlayers().get(0).card.toString().toLowerCase());
                buttonName1.setText(gui.getClient().getPlayers().get(0).name + "\n" + gui.getClient().getPlayers().get(0).card.toString());
                buttonGod3.getStyleClass().addAll("button",gui.getClient().getPlayers().get(1).card.toString().toLowerCase());
                buttonName3.setText(gui.getClient().getPlayers().get(1).name + "\n" + gui.getClient().getPlayers().get(1).card.toString());
            }

            buttonGod2.getStyleClass().addAll("button",gui.getClient().getMyPlayer().card.toString().toLowerCase());
            buttonSelected2.getStyleClass().addAll("button","podiumGold");
            buttonName2.setText(gui.getClient().getMyPlayer().name + "\n" + gui.getClient().getMyPlayer().card);

            buttonName1.setLayoutX(start + (gui.sceneWidth-start)/4 - buttonName1.getPrefWidth()/2-offset);
            buttonName2.setLayoutX(start + (gui.sceneWidth-start)/2 - buttonName2.getPrefWidth()/2);
            buttonName3.setLayoutX(start + 3*(gui.sceneWidth-start)/4 - buttonName3.getPrefWidth()/2+offset);

            buttonSelected1.setLayoutX(start + (gui.sceneWidth-start)/4 - buttonSelected1.getPrefWidth()/2-offset);
            buttonSelected2.setLayoutX(start + (gui.sceneWidth-start)/2 - buttonSelected2.getPrefWidth()/2);
            buttonSelected3.setLayoutX(start + 3*(gui.sceneWidth-start)/4 - buttonSelected3.getPrefWidth()/2+offset);

            buttonGod1.setLayoutX(start + (gui.sceneWidth-start)/4 - buttonGod1.getPrefWidth()/2-offset);
            buttonGod2.setLayoutX(start + (gui.sceneWidth-start)/2 - buttonGod2.getPrefWidth()/2);
            buttonGod3.setLayoutX(start + 3*(gui.sceneWidth-start)/4 - buttonGod3.getPrefWidth()/2+offset);

            buttonName2.getStyleClass().add(3,"nameSelected");
        } else if(gui.getClient().getPlayers().size() == 2){
            int offset = 30;

            if(gui.getClient().getMyPlayer().name.equals(gui.getClient().getPlayers().get(0).name)){
                buttonGod2.getStyleClass().addAll("button",gui.getClient().getPlayers().get(1).card.toString().toLowerCase());
                buttonName2.setText(gui.getClient().getPlayers().get(1).name + "\n" + gui.getClient().getPlayers().get(1).card.toString());
            } else if(gui.getClient().getMyPlayer().name.equals(gui.getClient().getPlayers().get(1).name)){
                buttonGod2.getStyleClass().addAll("button",gui.getClient().getPlayers().get(0).card.toString().toLowerCase());
                buttonName2.setText(gui.getClient().getPlayers().get(0).name + "\n" + gui.getClient().getPlayers().get(0).card.toString());
            }

            buttonGod1.getStyleClass().addAll("button",gui.getClient().getMyPlayer().card.toString().toLowerCase());
            buttonSelected1.getStyleClass().addAll("button","podiumGold");
            buttonName1.setText(gui.getClient().getMyPlayer().name + "\n" + gui.getClient().getMyPlayer().card);

            buttonName1.setLayoutX(start + (gui.sceneWidth-start)/3 - buttonName1.getPrefWidth()/2-offset);
            buttonName2.setLayoutX(start + 2*(gui.sceneWidth-start)/3 - buttonName2.getPrefWidth()/2+offset);

            buttonSelected1.setLayoutX(start + (gui.sceneWidth-start)/3 - buttonSelected1.getPrefWidth()/2-offset);
            buttonSelected2.setLayoutX(start + 2*(gui.sceneWidth-start)/3 - buttonSelected1.getPrefWidth()/2+offset);

            buttonGod1.setLayoutX(start + (gui.sceneWidth-start)/3 - buttonGod1.getPrefWidth()/2-offset);
            buttonGod2.setLayoutX(start + 2*(gui.sceneWidth-start)/3 - buttonGod2.getPrefWidth()/2+offset);
            buttonGod3.setVisible(false); buttonName3.setVisible(false); buttonSelected3.setVisible(false);

            buttonName1.getStyleClass().add(3,"nameSelected");
        }

        buttonSelected1.setLayoutY(210);
        buttonSelected2.setLayoutY(210);
        buttonSelected3.setLayoutY(210);
        buttonGod1.setLayoutY(100);
        buttonGod2.setLayoutY(100);
        buttonGod3.setLayoutY(100);

        cell00.getStyleClass().add("board");
        cell01.getStyleClass().add("board");
        cell02.getStyleClass().add("board");
        cell03.getStyleClass().add("board");
        cell04.getStyleClass().add("board");
        cell10.getStyleClass().add("board");
        cell11.getStyleClass().add("board");
        cell12.getStyleClass().add("board");
        cell13.getStyleClass().add("board");
        cell14.getStyleClass().add("board");
        cell20.getStyleClass().add("board");
        cell21.getStyleClass().add("board");
        cell22.getStyleClass().add("board");
        cell23.getStyleClass().add("board");
        cell24.getStyleClass().add("board");
        cell30.getStyleClass().add("board");
        cell31.getStyleClass().add("board");
        cell32.getStyleClass().add("board");
        cell33.getStyleClass().add("board");
        cell34.getStyleClass().add("board");
        cell40.getStyleClass().add("board");
        cell41.getStyleClass().add("board");
        cell42.getStyleClass().add("board");
        cell43.getStyleClass().add("board");
        cell44.getStyleClass().add("board");
        yes.getStyleClass().add("cl");
        no.getStyleClass().add("cl");
        setUpBanner(banner);
        setDescription(gui.getClient().getMyPlayer().card.name());
        state = 4;
    }

    public void god1OnAction(ActionEvent actionEvent) {
        setDescription(buttonName1.getText().split("\n")[1]);

        while(buttonName3.getStyleClass().size() > 3)
            buttonName3.getStyleClass().remove(3);
        while(buttonName2.getStyleClass().size() > 3)
            buttonName2.getStyleClass().remove(3);

        buttonName1.getStyleClass().add(3,"nameSelected");
    }
    public void god2OnAction(ActionEvent actionEvent) {
        setDescription(buttonName2.getText().split("\n")[1]);

        while(buttonName1.getStyleClass().size() > 3)
            buttonName1.getStyleClass().remove(3);
        while(buttonName3.getStyleClass().size() > 3)
            buttonName3.getStyleClass().remove(3);

        buttonName2.getStyleClass().add(3,"nameSelected");
    }
    public void god3OnAction(ActionEvent actionEvent) {
        setDescription(buttonName3.getText().split("\n")[1]);

        while(buttonName1.getStyleClass().size() > 3)
            buttonName1.getStyleClass().remove(3);
        while(buttonName2.getStyleClass().size() > 3)
            buttonName2.getStyleClass().remove(3);

        buttonName3.getStyleClass().add(3,"nameSelected");
    }

    public void setDescription(String name){
        try{
            String[] splitted = new String[0];
            for(SnapPlayer p : gui.getClient().getPlayers())
                if(p.card.toString().equals(name))
                    splitted = p.card.getDescription().split(" ");
            String desc = "";
            int cc = 0;
            for(int i=0; i<splitted.length; i++){
                cc += splitted[i].length();
                desc += splitted[i];
                if(cc > 30){
                    cc = 0;
                    desc += (i+1 == splitted.length ? "" : "\n");
                } else desc += " ";
            }
            buttonDescription.setText(desc);
        } catch (Exception ex){
            buttonDescription.setText("");
        }
    }

    public Image getConstruction(int lvl){
        if(lvl == 1)
            return floor1;
        if(lvl == 2)
            return floor2;
        if(lvl == 3)
            return floor3;
        if(lvl == 4)
            return dome;
        return null;
    }

    /**
     * @param x row coordinate
     * @param y column coordinate
     * @return the button (On Stackpane for buttons) with coordinate(x,y)
     */
    public Button getCell(int x, int y){
        if(x == 0 && y == 0)
            return cell00;
        if(x == 0 && y == 1)
            return cell01;
        if(x == 0 && y == 2)
            return cell02;
        if(x == 0 && y == 3)
            return cell03;
        if(x == 0 && y == 4)
            return cell04;
        if(x == 1 && y == 0)
            return cell10;
        if(x == 1 && y == 1)
            return cell11;
        if(x == 1 && y == 2)
            return cell12;
        if(x == 1 && y == 3)
            return cell13;
        if(x == 1 && y == 4)
            return cell14;
        if(x == 2 && y == 0)
            return cell20;
        if(x == 2 && y == 1)
            return cell21;
        if(x == 2 && y == 2)
            return cell22;
        if(x == 2 && y == 3)
            return cell23;
        if(x == 2 && y == 4)
            return cell24;
        if(x == 3 && y == 0)
            return cell30;
        if(x == 3 && y == 1)
            return cell31;
        if(x == 3 && y == 2)
            return cell32;
        if(x == 3 && y == 3)
            return cell33;
        if(x == 3 && y == 4)
            return cell34;
        if(x == 4 && y == 0)
            return cell40;
        if(x == 4 && y == 1)
            return cell41;
        if(x == 4 && y == 2)
            return cell42;
        if(x == 4 && y == 3)
            return cell43;
        if(x == 4 && y == 4)
            return cell44;
        return null;
    }

    /**
     * @param x row coordinate
     * @param y column coordinate
     * @return the sqaure (StackPane for Pawns ImageView) with coordinate(x,y)
     */
    public ImageView getSquare(int x, int y){
        if(x == 0 && y == 0)
            return square00;
        if(x == 0 && y == 1)
            return square01;
        if(x == 0 && y == 2)
            return square02;
        if(x == 0 && y == 3)
            return square03;
        if(x == 0 && y == 4)
            return square04;
        if(x == 1 && y == 0)
            return square10;
        if(x == 1 && y == 1)
            return square11;
        if(x == 1 && y == 2)
            return square12;
        if(x == 1 && y == 3)
            return square13;
        if(x == 1 && y == 4)
            return square14;
        if(x == 2 && y == 0)
            return square20;
        if(x == 2 && y == 1)
            return square21;
        if(x == 2 && y == 2)
            return square22;
        if(x == 2 && y == 3)
            return square23;
        if(x == 2 && y == 4)
            return square24;
        if(x == 3 && y == 0)
            return square30;
        if(x == 3 && y == 1)
            return square31;
        if(x == 3 && y == 2)
            return square32;
        if(x == 3 && y == 3)
            return square33;
        if(x == 3 && y == 4)
            return square34;
        if(x == 4 && y == 0)
            return square40;
        if(x == 4 && y == 1)
            return square41;
        if(x == 4 && y == 2)
            return square42;
        if(x == 4 && y == 3)
            return square43;
        if(x == 4 && y == 4)
            return square44;
        return null;
    }

    /**
     * @param x row coordinate
     * @param y column coordinate
     * @return the sqaure (StackPane With Tower's ImageView) with coordinate(x,y)
     */
        public ImageView getBLock(int x, int y){
        if(x == 0 && y == 0)
            return block00;
        if(x == 0 && y == 1)
            return block01;
        if(x == 0 && y == 2)
            return block02;
        if(x == 0 && y == 3)
            return block03;
        if(x == 0 && y == 4)
            return block04;
        if(x == 1 && y == 0)
            return block10;
        if(x == 1 && y == 1)
            return block11;
        if(x == 1 && y == 2)
            return block12;
        if(x == 1 && y == 3)
            return block13;
        if(x == 1 && y == 4)
            return block14;
        if(x == 2 && y == 0)
            return block20;
        if(x == 2 && y == 1)
            return block21;
        if(x == 2 && y == 2)
            return block22;
        if(x == 2 && y == 3)
            return block23;
        if(x == 2 && y == 4)
            return block24;
        if(x == 3 && y == 0)
            return block30;
        if(x == 3 && y == 1)
            return block31;
        if(x == 3 && y == 2)
            return block32;
        if(x == 3 && y == 3)
            return block33;
        if(x == 3 && y == 4)
            return block34;
        if(x == 4 && y == 0)
            return block40;
        if(x == 4 && y == 1)
            return block41;
        if(x == 4 && y == 2)
            return block42;
        if(x == 4 && y == 3)
            return block43;
        if(x == 4 && y == 4)
            return block44;
        return null;
    }

    /**
     * refresh the scene, for towers,buttons and pawns update
     */
    public void refresh() {

        for (SnapCell cell : this.gui.getClient().getBoard()) {
            if(cell.level != 0) {
                setLevel(getBLock(cell.row, cell.column), getConstruction(cell.level));
            }
        }
        //clean old workers position
        for (SnapCell cell : this.gui.getClient().getBoard()) {
                getSquare(cell.row, cell.column).setImage(null);

        }
        //get new workers position
        for(SnapPlayer p : this.gui.getClient().getPlayers())
            for (SnapWorker w : this.gui.getClient().getWorkers()){
                if(w.name == p.name) {
                    Image p2 = new Image(p.color, pawnSize, pawnSize, false, false);
                    getSquare(w.row,w.column).setImage(p2);
                }
            }
    }

    /**
     * Setter for set the image of the floor to a tower
     * @param block Imageview for load towers Image
     * @param floor Images of towers floors
     */
    public void setLevel(ImageView block, Image floor) {
        block.setImage(floor);
    }


    /**
     * Setter fot set the image of the player's pawn into their square Imageview
     * @param square Imageview for load towers Image
     */
    public void setPawn(ImageView square) {
        Image p1 = new Image(this.gui.getClient().getMyPlayer().color, pawnSize,pawnSize, false,false);
                square.setImage(p1);
    }

    /**
     * set the state of the buttons to change sendMessage generated once clicked
     * @param state
     */
    public void setState(int state) {
        this.state = state;
    }



    public boolean WorkerInitialize(int x, int y) {
         for (SnapWorker sw : this.gui.getClient().getWorkers()) {
             if (sw.row == x && sw.column == y) {
                 return false;
             }
         }
         this.gui.getClient().sendMessage(new WorkerInitializeClient(this.gui.getClient().getUsername(), x, y));
         return true;
    }

    public boolean ChoseWorker(int x, int y) {
        for (SnapWorker sw : this.gui.getClient().getWorkers()) {
            if (sw.row == x && sw.column == y && sw.name.equals(this.gui.getClient().getUsername()) && (Css.worker1 && sw.n == 1 || Css.worker2 && sw.n == 2)) {
                this.gui.getClient().sendMessage(new WorkerChoseClient(this.gui.getClient().getUsername(), sw.n));
                return true;
            }
        }
        return false;
    }


    public boolean Move(int x, int y) {
        for (SnapCell cell : Cms.sc) {
            if (cell.row == x && cell.column == y) {
                this.gui.getClient().sendMessage(new MoveClient(this.gui.getClient().getUsername(), x, y));
                        for (SnapCell cellx : Cms.sc) {
                            lighitdown(getCell(cellx.row,cellx.column));
                        }
                        return true;
            }
        }
        return false;
    }

    public boolean Build(int x, int y) {
        for (SnapCell cell : Cbs.sc) {
            if (cell.row == x && cell.column == y) {
                this.gui.getClient().sendMessage(new BuildClient(this.gui.getClient().getUsername(), x, y));
                    for (SnapCell cellx : Cbs.sc) {
                        lighitdown(getCell(cellx.row,cellx.column));
                    }
                    return true;
            }
        }
        return false;
    }

    public void setFloor(int x,int y) {
        for (SnapCell cell : this.gui.getClient().getBoard()) {
            if (cell.row == x && cell.column == y) {
                if (cell.level == 1) {
                    getBLock(x, y).setImage(floor1);
                }
                if (cell.level == 2) {
                    getBLock(x, y).setImage(floor2);
                }
                if (cell.level == 3) {
                    getBLock(x, y).setImage(floor3);
                }
                if (cell.level == 4) {
                    getBLock(x, y).setImage(dome);
                }
            }

        }
    }

    /**
     * Change the Css to set the yellow color of the active cells (buttons) after a getCheckMove or getCheckBuild
     * @param Cell the cell of the button
     */
    public void lighitup(Button Cell){
        Cell.getStyleClass().remove("board");
        Cell.getStyleClass().add("boardL");
    }

    /**
     * Change the Css to remove the yellow color of the active cells (buttons) after a getCheckMove or getCheckBuild
     * @param Cell the cell of the button
     */
    public void lighitdown(Button Cell){
        Cell.getStyleClass().remove("boardL");
        Cell.getStyleClass().add("board");
    }

    /**
     * Change the Css to make visible the YES button after an QuestionAbilityServer
     * @param Cell the cell of the button
     */
    public void yOn(Button Cell){
        Cell.getStyleClass().remove("cl");
        Cell.getStyleClass().add("yes");
    }

    /**
     * Change the Css to make invisible the YES button after an QuestionAbilityServer
     * @param Cell the cell of the button
     */
    public void yOff(Button Cell){
        Cell.getStyleClass().remove("yes");
        Cell.getStyleClass().add("cl");
    }

    /**
     * Change the Css to make visible the NO button after an QuestionAbilityServer
     * @param Cell the cell of the button
     */
    public void nOn(Button Cell){
        Cell.getStyleClass().remove("cl");
        Cell.getStyleClass().add("no");
    }

    /**
     * Change the Css to make invisible the NO button after an QuestionAbilityServer
     * @param Cell the cell of the button
     */
    public void nOff(Button Cell){
        Cell.getStyleClass().remove("no");
        Cell.getStyleClass().add("cl");
    }



    public void cell00(ActionEvent actionEvent) {
       int x = 0;
       int y = 0;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square00);
            setState(4);
            }
        }

        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)) {
                setState(4);
            }
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                block00.setImage(new Image("/it.polimi.ingsw/view/gui/img/tower/floor1.png"));
                setState(4);
            }
        }
    }

    public void cell01(ActionEvent actionEvent) {
        int x = 0;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square01);
            setState(4);
            }
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)) {
                setState(4);
            }
        }
        if(state == 2){//Move
            if(Move(x, y)){
                setState(4);
            }
        }
        if(state == 3) {  //Build
            if (Build(x, y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell02(ActionEvent actionEvent) {
        int x = 0;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square02);
            setState(4); }
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)) {
                setState(4);
            }
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell03(ActionEvent actionEvent) {
        int x = 0;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square03);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)) {
                setState(4);
            }
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell04(ActionEvent actionEvent) {
        int x = 0;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square04);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell10(ActionEvent actionEvent) {
        int x = 1;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square10);
            setState(4);}
        }

        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}

        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell11(ActionEvent actionEvent) {
        int x = 1;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square11);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell12(ActionEvent actionEvent) {
        int x = 1;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square12);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell13(ActionEvent actionEvent) {
        int x = 1;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square13);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell14(ActionEvent actionEvent) {
        int x = 1;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square14);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell20(ActionEvent actionEvent) {
        int x = 2;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square20);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell21(ActionEvent actionEvent) {
        int x = 2;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square21);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell22(ActionEvent actionEvent) {
        int x = 2;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square22);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell23(ActionEvent actionEvent) {
        int x = 2;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square23);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell24(ActionEvent actionEvent) {
        int x = 2;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square24);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell30(ActionEvent actionEvent) {
        int x = 3;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square30);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell31(ActionEvent actionEvent) {
        int x = 3;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square31);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell32(ActionEvent actionEvent) {
        int x = 3;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square32);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell33(ActionEvent actionEvent) {
        int x = 3;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square33);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell34(ActionEvent actionEvent) {
        int x = 3;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square34);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell40(ActionEvent actionEvent) {
        int x = 4;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square40);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell41(ActionEvent actionEvent) {
        int x = 4;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square41);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell42(ActionEvent actionEvent) {
        int x = 4;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square42);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }


    public void cell43(ActionEvent actionEvent) {
        int x = 4;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square43);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }

    public void cell44(ActionEvent actionEvent) {
        int x = 4;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            if(WorkerInitialize(x,y)){
            setPawn(square44);
            setState(4);}
        }
        if(state == 1){   //ChoseWorker
            if(ChoseWorker(x,y)){
            setState(4);}
        }
        if(state == 2){  //Move
            if(Move(x,y)) {
                setState(4);
            }
        }
        if(state == 3){  //Build
            if(Build(x,y)) {
                setFloor(x,y);
                setState(4);
            }
        }
    }



    public void showcheckmove(CheckMoveServer sc) {
        int x, y;
        if (Cms.name.equals(this.gui.getClient().getUsername())) {
            for (SnapCell cell : Cms.sc) {
                x = cell.row;
                y = cell.column;
                    if(x == 0 && y == 0)
                        lighitup(cell00);
                    if(x == 0 && y == 1)
                        lighitup(cell01);
                    if(x == 0 && y == 2)
                        lighitup(cell02);
                    if(x == 0 && y == 3)
                        lighitup(cell03);
                    if(x == 0 && y == 4)
                        lighitup(cell04);
                    if(x == 1 && y == 0)
                        lighitup(cell10);
                    if(x == 1 && y == 1)
                        lighitup(cell11);
                    if(x == 1 && y == 2)
                        lighitup(cell12);
                    if(x == 1 && y == 3)
                        lighitup(cell13);
                    if(x == 1 && y == 4)
                        lighitup(cell14);
                    if(x == 2 && y == 0)
                        lighitup(cell20);
                    if(x == 2 && y == 1)
                        lighitup(cell21);
                    if(x == 2 && y == 2)
                        lighitup(cell22);
                    if(x == 2 && y == 3)
                        lighitup(cell23);
                    if(x == 2 && y == 4)
                        lighitup(cell24);
                    if(x == 3 && y == 0)
                        lighitup(cell30);
                    if(x == 3 && y == 1)
                        lighitup(cell31);
                    if(x == 3 && y == 2)
                        lighitup(cell32);
                    if(x == 3 && y == 3)
                        lighitup(cell33);
                    if(x == 3 && y == 4)
                        lighitup(cell34);
                    if(x == 4 && y == 0)
                        lighitup(cell40);
                    if(x == 4 && y == 1)
                        lighitup(cell41);
                    if(x == 4 && y == 2)
                        lighitup(cell42);
                    if(x == 4 && y == 3)
                        lighitup(cell43);
                    if(x == 4 && y == 4)
                        lighitup(cell44);
                    }
           }


    }



    public void showcheckbuild(CheckBuildServer sb) {
        int x, y;
        if (Cbs.name.equals(this.gui.getClient().getUsername())) {
            for (SnapCell cell : Cbs.sc) {
                x = cell.row;
                y = cell.column;
                    if(x == 0 && y == 0)
                        lighitup(cell00);
                    if(x == 0 && y == 1)
                        lighitup(cell01);
                    if(x == 0 && y == 2)
                        lighitup(cell02);
                    if(x == 0 && y == 3)
                        lighitup(cell03);
                    if(x == 0 && y == 4)
                        lighitup(cell04);
                    if(x == 1 && y == 0)
                        lighitup(cell10);
                    if(x == 1 && y == 1)
                        lighitup(cell11);
                    if(x == 1 && y == 2)
                        lighitup(cell12);
                    if(x == 1 && y == 3)
                        lighitup(cell13);
                    if(x == 1 && y == 4)
                        lighitup(cell14);
                    if(x == 2 && y == 0)
                        lighitup(cell20);
                    if(x == 2 && y == 1)
                        lighitup(cell21);
                    if(x == 2 && y == 2)
                        lighitup(cell22);
                    if(x == 2 && y == 3)
                        lighitup(cell23);
                    if(x == 2 && y == 4)
                        lighitup(cell24);
                    if(x == 3 && y == 0)
                        lighitup(cell30);
                    if(x == 3 && y == 1)
                        lighitup(cell31);
                    if(x == 3 && y == 2)
                        lighitup(cell32);
                    if(x == 3 && y == 3)
                        lighitup(cell33);
                    if(x == 3 && y == 4)
                        lighitup(cell34);
                    if(x == 4 && y == 0)
                        lighitup(cell40);
                    if(x == 4 && y == 1)
                        lighitup(cell41);
                    if(x == 4 && y == 2)
                        lighitup(cell42);
                    if(x == 4 && y == 3)
                        lighitup(cell43);
                    if(x == 4 && y == 4)
                        lighitup(cell44);
                    }
            }

        }

    public void question(){
        yOn(yes);
        nOn(no);
        banner.setText(questionTA.getText());

    }

    public void yes(ActionEvent actionEvent) {
        if(yes.getStyleClass().toString() == "yes") {
            this.gui.getClient().sendMessage(new AnswerAbilityClient(this.gui.getClient().getUsername(), true, Qas.status));
            yOff(yes);
            nOff(no);
            refresh();
        }
    }

    public void no(ActionEvent actionEvent) {
        if(no.getStyleClass().toString() == "no") {
            this.gui.getClient().sendMessage(new AnswerAbilityClient(this.gui.getClient().getUsername(), false, Qas.status));
            yOff(yes);
            nOff(no);
            refresh();
        }
    }
}
