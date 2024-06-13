package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.UniqueKey;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users" ,uniqueConstraints = {@UniqueConstraint(columnNames = "userName")})
public class User extends BaseEntity {

  @Column
  private String fullName ;
  @Column(unique = true, nullable = false)
  private String userName ;
  @Column(nullable = false)
  private String password ;
  @Column
  private String address;
  @Column
  private String phoneNumber;
  @Column
  private String role ;
  @Column
  private boolean isActive= true;

  @ManyToMany(mappedBy = "users")
  private Collection<Product> product;

  @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
  private List<Bill> bills;

  @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
  private List<News> news;

  @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, optional = false)
  private Cart cart;

}
