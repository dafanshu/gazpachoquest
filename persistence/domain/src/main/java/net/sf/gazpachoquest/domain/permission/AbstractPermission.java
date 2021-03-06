package net.sf.gazpachoquest.domain.permission;

import java.lang.reflect.ParameterizedType;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import net.sf.gazpachoquest.domain.support.AbstractPersistable;
import net.sf.gazpachoquest.domain.support.Permission;
import net.sf.gazpachoquest.domain.support.Securizable;
import net.sf.gazpachoquest.domain.user.Role;
import net.sf.gazpachoquest.domain.user.User;
import net.sf.gazpachoquest.types.Perm;

@MappedSuperclass
public class AbstractPermission<T extends Securizable<?>> extends AbstractPersistable implements Permission<T> {

    private static final long serialVersionUID = 6599066653542691509L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    protected Role role;

    protected Integer mask;

    protected T target;

    public AbstractPermission() {
        super();
    }

    public AbstractPermission(Integer id, User user, Role role, Integer mask, T target) {
        super();
        super.setId(id);
        this.user = user;
        this.role = role;
        this.mask = mask;
        this.target = target;
    }

    @Override
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Integer getMask() {
        return mask;
    }

    public void setMask(Integer mask) {
        this.mask = mask;
    }

    @Override
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public T getTarget() {
        return target;
    }

    public void setTarget(T target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    @Transient
    @Override
    public String getLiteral() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> targetClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
        return new StringBuilder().append(targetClass.getSimpleName().toLowerCase()).append(":")
                .append(Perm.getLiteral(mask)).append(":").append(this.target.getId()).toString();
    }
}
