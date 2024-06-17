package homework5.domain.bank;

import homework5.domain.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@EqualsAndHashCode(of = "number")
@ToString(exclude = "customer")
@NoArgsConstructor
@Entity
@Table(name = "accounts")
@NamedEntityGraph(
        name = "accountWithCustomerAndCustomerEmployers",
        attributeNodes = {
                @NamedAttributeNode(value = "customer", subgraph = "customerGraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "customerGraph",
                        attributeNodes = {
                                @NamedAttributeNode("employers"),
                                @NamedAttributeNode("accounts")
                        }
                )
        }
)
public class Account extends AbstractEntity {

    @Column(name = "number", unique = true, nullable = false, updatable = false)
    private UUID number;

    @Column(name = "currency", length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "balance", nullable = false)
    private double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public Account(Currency currency, Customer customer) {
        this.currency = currency;
        this.customer = customer;
        this.balance = 0.0;
    }
}
