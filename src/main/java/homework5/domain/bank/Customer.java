package homework5.domain.bank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import homework5.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "email")
@ToString
@NoArgsConstructor
@Entity
@Table(name = "customers")
@NamedEntityGraph(
        name = "customerWithEmployersAndAccounts",
        attributeNodes = {
                @NamedAttributeNode("employers"),
                @NamedAttributeNode("accounts")
        }
)
@JsonView
public class Customer extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "age", nullable = false)
    private Integer age;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Account> accounts = new HashSet<>();

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "customer_in_employers",
            joinColumns = @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "customer_employer_fk")),
            inverseJoinColumns = @JoinColumn(name = "employer_id", foreignKey = @ForeignKey(name = "employer_customer_fk")),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"customer_id", "employer_id"})
            }
    )
    private Set<Employer> employers = new HashSet<>();

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    public Customer(String name, String email, int age, String phoneNumber, String password) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
