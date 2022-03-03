package it.iris.ticket.core.Ticket;

import it.iris.ticket.core.IrisManagaer;
import it.iris.ticket.core.database.IrisDatabase;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import java.util.UUID;

public class Comment {
    @Getter @Setter public int TicketID;
    @Getter @Setter public int ID;
    @Getter @Setter public String Date;
    @Getter @Setter public UUID Author;
    @Getter @Setter public String Text;

    public Comment(){}

    public void getById(int ID){
        ArrayList<String> datas = new ArrayList<>();
        datas.add(String.valueOf(ID));
        ArrayList<TreeMap<String,Object>> results = IrisManagaer.Database.preSetQuery(IrisDatabase.QUERY_TYPE_COMMENT_BY_ID,datas);
        this.ID = (Integer) results.get(0).get("ID");
        this.Author = UUID.fromString((String)results.get(0).get("AUTHOR"));
        this.Date = (String) results.get(0).get("DATE");
        this.TicketID = (Integer) results.get(0).get("TICKETID");
        this.Text = (String) results.get(0).get("TEXT");
    }

    public void upload(){
        if(Date == null){
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
            this.Date = formatter.format(date);
        }
        ArrayList<String> datas = new ArrayList<>();
        datas.add(String.valueOf(TicketID));
        datas.add(Author.toString());
        datas.add(Text);
        datas.add(Date);

        IrisManagaer.Database.preSetUpdate(IrisDatabase.UPDATE_TYPE_CREATE_COMMENT,datas);
    }
}
