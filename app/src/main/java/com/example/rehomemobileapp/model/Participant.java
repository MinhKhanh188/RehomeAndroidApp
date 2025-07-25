package com.example.rehomemobileapp.model;

public class Participant {
    private String _id;
    private String name;
    private String email;

    public Participant() {
    }

    public Participant(String _id, String name, String email) {
        this._id = _id;
        this.name = name;
        this.email = email;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "_id=\'" + _id + '\'' +
                ", name=\'" + name + '\'' +
                ", email=\'" + email + '\'' +
                '}';
    }
}