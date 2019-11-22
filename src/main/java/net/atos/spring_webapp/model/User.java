package net.atos.spring_webapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity                     // obiekt mapowany na encję w bazie danych
@Table(name = "user")       // można podać nazwę tabeli (tutaj zostałaby stworzona właśnie z tą nazwą
public class User {
    @Id                     // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-inkrementacja
    @Column(name = "user_id")   // możemy podać jak konkretnie ma się nazywać kolumna - inaczej defaultowo będzie tak samo jak nazwa pola tylko bez rozpoznawania wielkości liter
    private Long userId;        // dobrze jest w zmiennych podawać typ obiektowy
    @Email                      // umożliwia walidację (regexp)
    @NotBlank                   // @NotNull + @NotEmpty = @NotBlank
    private String email;
//    @Pattern(regexp = "^[a-zA-Z0-9-_.]+[^#]$", message = "Password is not complient!")
//    @Min(value = 6)
//    @Size(min = 6, max = 255)
    private String password;
//    @DateTimeFormat(pattern = "YYYY-mm-dd HH:MM:SS")
    private LocalDateTime registerDate = LocalDateTime.now();
    @Basic(optional = true)
    private Boolean enable = true;
    @Transient // wykluczenie z mapowania do DB
    private Character gender;

    //nie ma znaczenia czy utworzymy relację w tym czy w drugim obiekcie - relacja jest dwustronna
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) //pobieranie zachlanne wszystkich zaleznosci /
    @JoinTable(
            name = "user_permission",
            joinColumns = @JoinColumn(name = "user_id"), //kolumna z User
            inverseJoinColumns = @JoinColumn(name = "permission_id") //kolumna z Permission
    )
    private Set<Permission> roles = new HashSet<>();

    @OneToMany(mappedBy = "author") //matchowanie po polu "author" z klasy Post
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<Message> messages = new ArrayList<>();


    public User(@Email @NotBlank String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void addPermission(Permission permission){
        this.roles.add(permission);
    }

    public String getRolesString(){
        String rolesString = "";
        for (Permission p : this.getRoles()) {
            rolesString += p.getRoleName() + " ";
        }
        return rolesString;
    }

}
