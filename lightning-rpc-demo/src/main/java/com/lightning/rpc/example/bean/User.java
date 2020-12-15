package com.lightning.rpc.example.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class User implements Serializable {

    private static final long serialVersionUID = 8026168121607551824L;

    private Integer id;

    private String name;



}
