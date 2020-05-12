package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.commons.SnapCell;
import it.polimi.ingsw.commons.SnapWorker;
import it.polimi.ingsw.commons.clientmessages.*;
import it.polimi.ingsw.commons.servermessages.CheckBuildServer;
import it.polimi.ingsw.commons.servermessages.CheckMoveServer;
import it.polimi.ingsw.commons.servermessages.CurrentStatusServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BoardController extends DefaultController {


    int towerSize = 80;
    int pawnSize = 40;

    int state = 4;
    CurrentStatusServer Css;
    CheckMoveServer Cms;
    CheckBuildServer Cbs;


    Image floor1 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor1.png", towerSize, towerSize, true, false);
    Image floor2 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor2.png", towerSize, towerSize, true, false);
    Image floor3 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor3.png", towerSize, towerSize, true, false);
    Image dome = new Image("/it.polimi.ingsw/view/gui/img/tower/dome.png", towerSize, towerSize, true, false);

    Image red = new Image("/it.polimi.ingsw/view/gui/img/pawn/pawn_red.png", pawnSize, pawnSize, false, false);
    Image blu = new Image("/it.polimi.ingsw/view/gui/img/pawn/pawn_blu.png", pawnSize, pawnSize, false, false);
    Image yellow = new Image("/it.polimi.ingsw/view/gui/img/pawn/pawn_yellow.png", pawnSize, pawnSize, false, false);
    Image bronze = new Image("/it.polimi.ingsw/view/gui/img/pawn/pawn_bronze.png", pawnSize, pawnSize, false, false);
    Image green = new Image("/it.polimi.ingsw/view/gui/img/pawn/pawn_green.png", pawnSize, pawnSize, false, false);


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
    }


    public void setLevel(ImageView block, Image floor) {
        block.setImage(floor);
    }


    public void setPawn(ImageView square, Image pawn) {
        square.setImage(pawn);
    }

    public void setState(int state) {
        this.state = state;
    }


    public void WorkerInitialize(int x, int y) {
        this.gui.getClient().sendMessage(new WorkerInitializeClient(this.gui.getClient().getUsername(), x, y));
    }

    public void ChoseWorker(int x, int y) {
        for (SnapWorker sw : this.gui.getClient().getWorkers()) {
            if (sw.row == 0 && sw.column == 0 && sw.name.equals(this.gui.getClient().getUsername()) && (Css.worker1 && sw.n == 1 || Css.worker2 && sw.n == 2)) {
                this.gui.getClient().sendMessage(new WorkerChoseClient(this.gui.getClient().getUsername(), sw.n));
            }
        }
    }


    public void Move(int x, int y) {
        for (SnapCell cell : Cms.sc) {
            if (cell.row == x && cell.column == y) {
                this.gui.getClient().sendMessage(new MoveClient(this.gui.getClient().getUsername(), x, y));
            }
        }
    }

    public void Build(int x, int y) {
        for (SnapCell cell : Cbs.sc) {
            if (cell.row == x && cell.column == y) {
                this.gui.getClient().sendMessage(new BuildClient(this.gui.getClient().getUsername(), x, y));
            }
        }
    }






    public void lighitup(Button Cell){
        Image image = new Image("/it.polimi.ingsw/view/gui/img/button/allow.png");
        Cell.setGraphic(new ImageView(image));
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
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);
        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell01(ActionEvent actionEvent) {
        int x = 0;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell02(ActionEvent actionEvent) {
        int x = 0;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell03(ActionEvent actionEvent) {
        int x = 0;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);
        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell04(ActionEvent actionEvent) {
        int x = 0;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }

    }

    public void cell10(ActionEvent actionEvent) {
        int x = 1;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell11(ActionEvent actionEvent) {
        int x = 1;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell12(ActionEvent actionEvent) {
        int x = 1;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell13(ActionEvent actionEvent) {
        int x = 1;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell14(ActionEvent actionEvent) {
        int x = 1;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell20(ActionEvent actionEvent) {
        int x = 2;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell21(ActionEvent actionEvent) {
        int x = 2;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell22(ActionEvent actionEvent) {
        int x = 2;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell23(ActionEvent actionEvent) {
        int x = 2;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell24(ActionEvent actionEvent) {
        int x = 2;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell30(ActionEvent actionEvent) {
        int x = 3;
        int y = 0;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell31(ActionEvent actionEvent) {
        int x = 3;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell32(ActionEvent actionEvent) {
        int x = 3;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell33(ActionEvent actionEvent) {
        int x = 3;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);

        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);

        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell34(ActionEvent actionEvent) {
        int x = 3;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);
        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);
        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell40(ActionEvent actionEvent) {
        int x = 0;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);
        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);
        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell41(ActionEvent actionEvent) {
        int x = 4;
        int y = 1;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);
        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);
        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell42(ActionEvent actionEvent) {
        int x = 4;
        int y = 2;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);
        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);
        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }


    public void cell43(ActionEvent actionEvent) {
        int x = 4;
        int y = 3;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);
        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);
        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
        }
    }

    public void cell44(ActionEvent actionEvent) {
        int x = 4;
        int y = 4;
        if(state == 0){   //WorkerInitialize
            WorkerInitialize(x,y);
            setState(4);
        }

        if(state == 1){   //ChoseWorker
            ChoseWorker(x,y);
            setState(4);
        }
        if(state == 2){  //Move
            Move(x,y);
            setState(4);
        }
        if(state == 3){  //Build
            Build(x,y);
            setState(4);
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

}