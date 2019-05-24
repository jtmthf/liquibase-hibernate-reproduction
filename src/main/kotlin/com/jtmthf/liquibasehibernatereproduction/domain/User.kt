package com.jtmthf.liquibasehibernatereproduction.domain

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.Parameter
import org.hibernate.annotations.Type
import org.hibernate.id.enhanced.SequenceStyleGenerator
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false, unique = true)
    @NaturalId
    var username: String,

    @Type(type = "text")
    var bio: String? = null,

    @Type(type = "text")
    var image: String? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userGenerator")
    @GenericGenerator(
        name = "userGenerator", strategy = "enhanced-sequence", parameters = [
            Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "50"),
            Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "user_sequence")
        ]
    )
    val id = -1L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        return username.hashCode()
    }
}