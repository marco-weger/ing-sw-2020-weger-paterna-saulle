package it.polimi.ingsw.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BoardController extends DefaultController {

    int tower_size = 80;
    Image floor1 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor1.png",tower_size,tower_size,true,false);
    Image floor2 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor2.png",tower_size,tower_size,true,false);
    Image floor3 = new Image("/it.polimi.ingsw/view/gui/img/tower/floor3.png",tower_size,tower_size,true,false);
    Image dome = new Image("/it.polimi.ingsw/view/gui/img/tower/dome.png",tower_size,tower_size,true,false);


    @FXML
    ImageView block00, block01, block02, block03, block04,
              block10, block11, block12, block13 ,block14,
              block20, block21, block22, block23, block24,
              block30, block31, block32, block33, block34,
              block40, block41, block42, block43, block44;





    @FXML
    @Override
    public void initialize() {
        super.initialize();
        super.setBackground(new Image("/it.polimi.ingsw/view/gui/img/scene/bg_match.png"));
    }


    public void setLevel(ImageView block, Image floor){
        block.setImage(floor);
    }


    public void cell00(ActionEvent actionEvent) {
    }

    public void cell01(ActionEvent actionEvent) {
    }

    public void cell02(ActionEvent actionEvent) {
    }

    public void cell03(ActionEvent actionEvent) {
    }

    public void cell04(ActionEvent actionEvent) {
    }

    public void cell10(ActionEvent actionEvent) {
    }

    public void cell11(ActionEvent actionEvent) {
    }

    public void cell12(ActionEvent actionEvent) {
    }

    public void cell13(ActionEvent actionEvent) {
    }

    public void cell14(ActionEvent actionEvent) {
    }

    public void cell20(ActionEvent actionEvent) {
    }

    public void cell21(ActionEvent actionEvent) {
    }

    public void cell22(ActionEvent actionEvent) {
    }

    public void cell23(ActionEvent actionEvent) {
    }

    public void cell24(ActionEvent actionEvent) {
    }

    public void cell30(ActionEvent actionEvent) {
    }

    public void cell31(ActionEvent actionEvent) {
    }

    public void cell32(ActionEvent actionEvent) {
    }

    public void cell33(ActionEvent actionEvent) {
    }

    public void cell34(ActionEvent actionEvent) {
    }

    public void cell40(ActionEvent actionEvent) {
    }

    public void cell41(ActionEvent actionEvent) {
    }

    public void cell42(ActionEvent actionEvent) {
    }


    public void cell43(ActionEvent actionEvent) {
    }

    public void cell44(ActionEvent actionEvent) {
    }

}