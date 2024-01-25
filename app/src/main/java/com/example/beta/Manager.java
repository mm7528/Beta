package com.example.beta;
import com.example.beta.User;

import java.util.List;

public class Manager {
    public List<User> registeredUsers;

    public Manager()
    {

    }
    public User getUser(String uid)
    {
        for(int i=0;i<registeredUsers.size();i++)
        {
            if(registeredUsers.get(i).uid.equals(uid))
            {
                return registeredUsers.get(i);
            }
        }
        return null;
    }

}
