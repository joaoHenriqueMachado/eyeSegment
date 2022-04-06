package com.mygdx.teste;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Node {
    private Node next;
    private Node prev;
    private Image image;
    private String imageName;
    private image.Image exportImage;

    public Node(Image image, String imageName, image.Image exportImage){
        this.image = image;
        this.next = null;
        this.prev = null;
        this.imageName = imageName;
        this.exportImage = exportImage;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public image.Image getExportImage() {
        return exportImage;
    }

    public void setExportImage(image.Image exportImage) {
        this.exportImage = exportImage;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}
