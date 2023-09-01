package fr.iutna.lpmiar.td1.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "USERS")
class User(
    var nom: String,
    var prenom: String,
    @Column(unique = true) var email: String,
    @JsonIgnore private var password: String,
    @Enumerated(EnumType.STRING) var roles: Role
): UserDetails {

    @Id
    var id: Int? = null

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnoreProperties("user")
    val reservations: List<Reservation> = mutableListOf()

    @ManyToMany
    @JoinTable(
        name = "user_groupe",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "groupe_id")]
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    val groupeFavoris: MutableList<Groupe> = mutableListOf()

    @ManyToMany
    @JoinTable(
        name = "user_salle",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "salle_id")]
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    val salleFavoris: MutableList<Salle> = mutableListOf()

    @ManyToMany
    @JoinTable(
        name = "user_etudiant",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "etudiant_id")]
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    val etudiantFavoris: MutableList<Etudiant> = mutableListOf()

    @ManyToMany
    @JoinTable(
        name = "user_personnel",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "personnel_id")]
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    val personnelFavoris: MutableList<Personnel> = mutableListOf()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(roles.name))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun toString(): String {
        return "User(id=$id, nom=$nom, prenom=$prenom, email=$email, password=$password, roles=$roles)"
    }


}
