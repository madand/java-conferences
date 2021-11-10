package net.madand.conferences.entity;

/**
 * Interface for entities that provide read/write access to their integer ID (a primary key).
 */
public interface IDable {
    int getId();
    void setId(int id);
}
