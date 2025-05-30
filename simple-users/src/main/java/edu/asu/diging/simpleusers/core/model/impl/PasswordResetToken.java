package edu.asu.diging.simpleusers.core.model.impl;

import java.time.OffsetDateTime;


import edu.asu.diging.simpleusers.core.model.IPasswordResetToken;
import edu.asu.diging.simpleusers.core.model.IUser;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class PasswordResetToken implements IPasswordResetToken {

    @Id
    @GeneratedValue(generator="token_generator", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="token_generator")
    private Long id;
    
    private String token;
    
    @OneToOne(targetEntity = SimpleUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private IUser user;
  
    private OffsetDateTime expiryDate;

    /* (non-Javadoc)
     * @see edu.asu.diging.simpleusers.core.model.impl.IPasswordResetToken#getId()
     */
    @Override
    public Long getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.simpleusers.core.model.impl.IPasswordResetToken#setId(java.lang.Long)
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.simpleusers.core.model.impl.IPasswordResetToken#getToken()
     */
    @Override
    public String getToken() {
        return token;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.simpleusers.core.model.impl.IPasswordResetToken#setToken(java.lang.String)
     */
    @Override
    public void setToken(String token) {
        this.token = token;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.simpleusers.core.model.impl.IPasswordResetToken#getUser()
     */
    @Override
    public IUser getUser() {
        return user;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.simpleusers.core.model.impl.IPasswordResetToken#setUser(edu.asu.diging.simpleusers.core.model.IUser)
     */
    @Override
    public void setUser(IUser user) {
        this.user = user;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.simpleusers.core.model.impl.IPasswordResetToken#getExpiryDate()
     */
    @Override
    public OffsetDateTime getExpiryDate() {
        return expiryDate;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.simpleusers.core.model.impl.IPasswordResetToken#setExpiryDate(java.time.OffsetDateTime)
     */
    @Override
    public void setExpiryDate(OffsetDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
    
}
