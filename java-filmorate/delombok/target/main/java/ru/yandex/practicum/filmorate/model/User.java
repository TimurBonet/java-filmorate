package ru.yandex.practicum.filmorate.model;

import lombok.NonNull;
import javax.validation.constraints.Email;
import java.time.LocalDate;

public class User {
    private int id;
    @NonNull
    @Email
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;

    @lombok.Generated
    public User(@NonNull final String email, @NonNull final String login, @NonNull final LocalDate birthday) {
        if (email == null) {
            throw new NullPointerException("email is marked non-null but is null");
        }
        if (login == null) {
            throw new NullPointerException("login is marked non-null but is null");
        }
        if (birthday == null) {
            throw new NullPointerException("birthday is marked non-null but is null");
        }
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

    @lombok.Generated
    public int getId() {
        return this.id;
    }

    @NonNull
    @lombok.Generated
    public String getEmail() {
        return this.email;
    }

    @NonNull
    @lombok.Generated
    public String getLogin() {
        return this.login;
    }

    @lombok.Generated
    public String getName() {
        return this.name;
    }

    @NonNull
    @lombok.Generated
    public LocalDate getBirthday() {
        return this.birthday;
    }

    @lombok.Generated
    public void setId(final int id) {
        this.id = id;
    }

    @lombok.Generated
    public void setEmail(@NonNull final String email) {
        if (email == null) {
            throw new NullPointerException("email is marked non-null but is null");
        }
        this.email = email;
    }

    @lombok.Generated
    public void setLogin(@NonNull final String login) {
        if (login == null) {
            throw new NullPointerException("login is marked non-null but is null");
        }
        this.login = login;
    }

    @lombok.Generated
    public void setName(final String name) {
        this.name = name;
    }

    @lombok.Generated
    public void setBirthday(@NonNull final LocalDate birthday) {
        if (birthday == null) {
            throw new NullPointerException("birthday is marked non-null but is null");
        }
        this.birthday = birthday;
    }

    @Override
    @lombok.Generated
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;
        final User other = (User) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getId() != other.getId()) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$login = this.getLogin();
        final Object other$login = other.getLogin();
        if (this$login == null ? other$login != null : !this$login.equals(other$login)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$birthday = this.getBirthday();
        final Object other$birthday = other.getBirthday();
        if (this$birthday == null ? other$birthday != null : !this$birthday.equals(other$birthday)) return false;
        return true;
    }

    @lombok.Generated
    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    @Override
    @lombok.Generated
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getId();
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $login = this.getLogin();
        result = result * PRIME + ($login == null ? 43 : $login.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $birthday = this.getBirthday();
        result = result * PRIME + ($birthday == null ? 43 : $birthday.hashCode());
        return result;
    }

    @Override
    @lombok.Generated
    public String toString() {
        return "User(id=" + this.getId() + ", email=" + this.getEmail() + ", login=" + this.getLogin() + ", name=" + this.getName() + ", birthday=" + this.getBirthday() + ")";
    }
}
