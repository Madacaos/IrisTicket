package it.iris.ticket.core.Ticket;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Comment {
    @Getter @Setter public int TicketID;
    @Getter @Setter public Date Date;
    @Getter @Setter public UUID PlayerUUID;
    @Getter @Setter public String Text;

    public Comment(){}

    public void createNew(int TicketID, Date Date, UUID PlayerUUID, String Text){
        this.TicketID = TicketID;
        this.Date = Date;
        this.PlayerUUID = PlayerUUID;
        this.Text = Text;
    }

    public void upload(){

    }
}
