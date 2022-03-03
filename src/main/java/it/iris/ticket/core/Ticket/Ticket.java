package it.iris.ticket.core.Ticket;

import it.iris.ticket.core.Core;
import it.iris.ticket.core.IrisManagaer;
import it.iris.ticket.core.database.IrisDatabase;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import java.util.UUID;

public class Ticket {
    @Setter @Getter int ID;
    @Setter @Getter UUID Author;
    @Setter @Getter String Date;
    @Setter @Getter String Category;
    @Setter @Getter boolean isOpen;

    @Setter @Getter public ArrayList<Comment> Comments;

    public Ticket(){}

    public void getById(int ID){
        ArrayList<String> datas = new ArrayList<>();
        datas.add(String.valueOf(ID));
        ArrayList<TreeMap<String,Object>> results = IrisManagaer.Database.preSetQuery(IrisDatabase.QUERY_TYPE_TICKET_BY_ID,datas);
        this.ID = (Integer) results.get(0).get("ID");
        this.Author = UUID.fromString((String)results.get(0).get("AUTHOR"));
        this.Date = (String) results.get(0).get("DATE");
        this.Category = (String) results.get(0).get("CATEGORY");
        this.isOpen = Boolean.parseBoolean((String) results.get(0).get("OPENED"));
    }

    public void getComments(){
        this.Comments = new ArrayList<>();
        ArrayList<String> datas = new ArrayList<>();
        datas.add(String.valueOf(ID));
        ArrayList<TreeMap<String,Object>> results = IrisManagaer.Database.preSetQuery(IrisDatabase.QUERY_TYPE_COMMENTS_BY_TICKETID,datas);
        for(TreeMap<String,Object> result : results){
            Comment comment = new Comment();

            comment.setAuthor(UUID.fromString((String) result.get("AUTHOR")));
            comment.setTicketID((Integer) result.get("TICKETID"));
            comment.setText((String) result.get("TEXT"));
            comment.setID((Integer) result.get("ID"));

            Comments.add(comment);
        }
    }

    public void upload(){
        if(Date == null) {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
            this.Date = formatter.format(date);
        }
        ArrayList<String> datas = new ArrayList<>();
        datas.add(Author.toString());
        datas.add(Date);
        datas.add(Category);
        datas.add(String.valueOf(isOpen));

        ArrayList<String> datas2 = new ArrayList<>();
        datas2.add("ID");

        IrisManagaer.Database.preSetUpdate(IrisDatabase.UPDATE_TYPE_CREATE_TICKET,datas);
        ArrayList<TreeMap<String,Object>> results = IrisManagaer.Database.executeQuery("SELECT * FROM TICKETLIST WHERE DATE='"+Date+"' AND AUTHOR='"+Author.toString()+"' AND CATEGORY ='"+Category+"'",datas2);

        setID((Integer) results.get(results.size()-1).get("ID"));
    }
}
