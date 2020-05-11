package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.clientmessages.ConnectionClient;
import it.polimi.ingsw.commons.clientmessages.WorkerChoseClient;
import it.polimi.ingsw.commons.clientmessages.WorkerInitializeClient;
import it.polimi.ingsw.commons.servermessages.CurrentStatusServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BoardController extends DefaultController {


    int towerSize = 80;
    int pawnSize = 40;

    int state = 4;
    CurrentStatusServer Css;


    Image floor1 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor1.png", towerSize, towerSize,true,false);
    Image floor2 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor2.png", towerSize, towerSize,true,false);
    Image floor3 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor3.png", towerSize, towerSize,true,false);
    Image dome = new Image("/it.polimi.ingsw/view/gui/img/tower/dome.png", towerSize, towerSize,true,false);

    Image red = new Image("/it.polimi.ingsw/view/gui/img/pawn/pawn_red.png", pawnSize, pawnSize,false,false);
    Image blu = new Image("/it.polimi.ingsw/view/gui/img/pawn/pawn_blu.png", pawnSize, pawnSize,false,false);
    Image yellow = new Image("/it.polimi.ingsw/view/gui/img/pawn/pawn_yellow.png", pawnSize, pawnSize,false,false);
    Image bronze = new Image("/it.polimi.ingsw/view/gui/img/pawn/pawn_bronze.png", pawnSize, pawnSize,false,false);
    Image green = new Image("/it.polimi.ingsw/view/gui/img/pawn/pawn_green.png", pawnSize, pawnSize,false,false);



    @FXML
    ImageView block00, block01, block02, block03, block04,
              block10, block11, block12, block13 ,block14,
              block20, block21, block22, block23, block24,
              block30, block31, block32, block33, block34,
              block40, block41, block42, block43, block44;

     @FXML
     ImageView square00,square01,square02,square03,square04,
               square10,square11,square12,square13,square14,
               square20,square21,square22,square23,square24,
               square30,square31,square32,square33,square34,
               square40,square41,square42,square43,square44;






    @FXML
    @Override
    public void initialize() {
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_match.png"));
    }


    public void setLevel(ImageView block, Image floor){
        block.setImage(floor);
    }


    public void setPawn(ImageView square, Image pawn){
        square.setImage(pawn);
    }

    public void setState(int state) {
        this.state = state;
    }



    public void WorkerInitialize(int x, int y){
        this.gui.getClient().sendMessage(new WorkerInitializeClient(this.gui.getClient().getUsername(),x,y));
    }

    public void ChoseWorker(int x,int y){
        for(SnapWorker sw : this.gui.getClient().getWorkers()) {
            if (sw.row == 0 && sw.column == 0 && sw.name.equals(this.gui.getClient().getUsername()) && (Css.worker1 && sw.n == 1 || Css.worker2 && sw.n == 2)) {
                this.gui.getClient().sendMessage(new WorkerChoseClient(this.gui.getClient().getUsername(), sw.n));
            }
        }
    }

    public void Move(int x, int y){
        //TODO Move
            System.out.println(x +y);
    }

    public void Build(int x, int y){
        //TODO Build
            System.out.println(x + y);
    }






    /**
     * @param actionEvent click event
     *
     * [ 0 = WorkerInitialize |  1 = WorkerChose |  2 = Move  | 3 = Build  | 4 = Wait ]
     */


    public void cell00(ActionEvent actionEvent) {
       int x = 0;
       int y = 0;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell01(ActionEvent actionEvent) {
        int x = 0;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell02(ActionEvent actionEvent) {
        int x = 0;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell03(ActionEvent actionEvent) {
        int x = 0;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell04(ActionEvent actionEvent) {
        int x = 0;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }

    }

    public void cell10(ActionEvent actionEvent) {
        int x = 1;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell11(ActionEvent actionEvent) {
        int x = 1;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell12(ActionEvent actionEvent) {
        int x = 1;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell13(ActionEvent actionEvent) {
        int x = 1;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell14(ActionEvent actionEvent) {
        int x = 1;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell20(ActionEvent actionEvent) {
        int x = 2;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell21(ActionEvent actionEvent) {
        int x = 2;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell22(ActionEvent actionEvent) {
        int x = 2;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell23(ActionEvent actionEvent) {
        int x = 2;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell24(ActionEvent actionEvent) {
        int x = 2;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell30(ActionEvent actionEvent) {
        int x = 3;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell31(ActionEvent actionEvent) {
        int x = 3;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell32(ActionEvent actionEvent) {
        int x = 3;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell33(ActionEvent actionEvent) {
        int x = 3;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell34(ActionEvent actionEvent) {
        int x = 3;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell40(ActionEvent actionEvent) {
        int x = 0;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell41(ActionEvent actionEvent) {
        int x = 4;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell42(ActionEvent actionEvent) {
        int x = 4;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }


    public void cell43(ActionEvent actionEvent) {
        int x = 4;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

    public void cell44(ActionEvent actionEvent) {
        int x = 4;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);

        }
        if(state == 2){  //Move
            Move(x,y);

        }
        if(state == 3){  //Build
            Build(x,y);
        }
    }

}