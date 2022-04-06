package com.mygdx.teste;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class DoublyLinkedList {
    private Node head;

    public DoublyLinkedList(){
        head = null;
    }

    public boolean emptyList(){
        return (head == null);
    }

    public void insertLast(Image image, String imageName, image.Image exportImage){
        Node new_node = new Node(image, imageName, exportImage);
        Node last;
        if(this.emptyList()){
            new_node.setNext(new_node);
            new_node.setPrev(new_node);
            head = new_node;
        }
        else{
            last = head.getPrev();
            last.setNext(new_node);
            new_node.setPrev(last);
            new_node.setNext(head);
            head.setPrev(new_node);
        }
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public Node getHead() {
        return head;
    }
}
