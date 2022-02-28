package it.iris.ticket.core.Ticket;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Ticket {
    @Setter @Getter int ID;
    @Setter @Getter UUID Author;
    @Setter @Getter Date Date;
    @Setter @Getter boolean isOpen;

    @Setter @Getter public ArrayList<Comment> Comments;

    public Ticket(){}

    public void getById(int ID){

    }

    public void upload(){
    }
}
