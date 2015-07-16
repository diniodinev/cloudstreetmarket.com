package edu.zipcloud.cloudstreetmarket.core.entities;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import edu.zipcloud.cloudstreetmarket.core.enums.SupportedCurrency;

@Entity
@Table(name="users")
public class User extends AbstractId<String> implements UserDetails{

	private static final long serialVersionUID = 1990856213905768044L;

	private String fullname;
	
	private String email;
	
	private String password;
	
	private boolean enabled = true;
	
	private String profileImg;
	
	@Column(name="not_expired")
	private boolean accountNonExpired;
	
	@Column(name="not_locked")
	private boolean accountNonLocked;

	@Enumerated(EnumType.STRING)
	private SupportedCurrency currency;

	private BigDecimal balance;
	
	@OneToMany(mappedBy = "user", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	@OrderBy("id desc")
	private Set<Action> actions = new LinkedHashSet<Action>();

	@OneToMany(mappedBy = "user", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Set<Authority> authorities = new LinkedHashSet<Authority>();

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<SocialUser> socialUsers = new LinkedHashSet<SocialUser>();
	
	@Column(name="last_update", insertable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;

	public User(){
	
	}
	
	public User(String id){
		setId(id);
	}
	
	public User(String id, String password, String email, boolean enabled, boolean accountNonExpired,
			boolean accountNonLocked, boolean credentialNotExpired, Set<Authority> auth, SupportedCurrency currency, BigDecimal balance) {
		setId(id);
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = auth;
		this.currency = currency;
		this.balance = balance;
	}

	public User(User user, Set<Authority> authorities) {
		this(user.getUsername(), user.getPassword(), user.getEmail(), user.isEnabled(), user.isAccountNonExpired(), user.isAccountNonLocked(), true, authorities, user.getCurrency(), user.getBalance());
	}

	@Override
	public String getUsername() {
		return getId();
	}

	@Override
	public String getPassword() {
		return password;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(String profileImg) {
		this.profileImg = !profileImg.equals("img/anon.png") ? profileImg : null;
	}

	public Set<Action> getActions() {
		return actions;
	}

	public void setActions(Set<Action> actions) {
		this.actions = actions;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public SupportedCurrency getCurrency() {
		return currency;
	}

	public void setCurrency(SupportedCurrency currency) {
		this.currency = currency;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	public void addAuthority(Authority authority){
		authorities.add(authority);
	}
	
	public void addAction(Action action){
		actions.add(action);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	//Avoid fetching lazy collections here (session may be closed depending upon where toString is called from)
	@Override
	public String toString() {
		return "User [fullname=" + fullname + ", email=" + email
				+ ", password=" + password + ", enabled=" + enabled
				+ ", profileImg=" + profileImg + ", accountNonExpired="
				+ accountNonExpired + ", accountNonLocked=" + accountNonLocked
				+ ", currency=" + currency + ", balance=" +balance +", authorities=" + authorities
				+ ", lastUpdate=" + lastUpdate + ", id=" + id + "]";
	}
}