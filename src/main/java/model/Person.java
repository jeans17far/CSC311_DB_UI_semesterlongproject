package model;

/**
 * Represents a person in the Employee Management System.
 * Includes information such as name, department, performance rating, email, and an optional image URL.
 */
public class Person {

    private Integer id;
    private String firstName;
    private String lastName;
    private String department;
    private Double performanceRating;
    private String email;
    private String imageUrl;

    /**
     * Default constructor for creating an empty Person object.
     */
    public Person() {
        // No-argument constructor
    }

    /**
     * Constructor for creating a Person object without an ID.
     *
     * @param firstName        the first name of the person
     * @param lastName         the last name of the person
     * @param department       the department the person belongs to
     * @param performanceRating the performance rating of the person
     * @param email            the email of the person
     */
    public Person(String firstName, String lastName, String department, Double performanceRating, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.performanceRating = performanceRating;
        this.email = email;
    }

    /**
     * Constructor for creating a Person object with an ID.
     *
     * @param id               the unique identifier of the person
     * @param firstName        the first name of the person
     * @param lastName         the last name of the person
     * @param department       the department the person belongs to
     * @param performanceRating the performance rating of the person
     * @param email            the email of the person
     */
    public Person(Integer id, String firstName, String lastName, String department, Double performanceRating, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.performanceRating = performanceRating;
        this.email = email;
    }

    /**
     * Retrieves the ID of the person.
     *
     * @return the person's ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of the person.
     *
     * @param id the new ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retrieves the first name of the person.
     *
     * @return the person's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the person.
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieves the last name of the person.
     *
     * @return the person's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the person.
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Retrieves the department of the person.
     *
     * @return the person's department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department of the person.
     *
     * @param department the new department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Retrieves the performance rating of the person.
     *
     * @return the person's performance rating
     */
    public Double getPerformanceRating() {
        return performanceRating;
    }

    /**
     * Sets the performance rating of the person.
     *
     * @param performanceRating the new performance rating
     */
    public void setPerformanceRating(Double performanceRating) {
        this.performanceRating = performanceRating;
    }

    /**
     * Retrieves the email of the person.
     *
     * @return the person's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the person.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the image URL of the person.
     *
     * @return the person's image URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the image URL of the person.
     *
     * @param imageUrl the new image URL
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Provides a string representation of the person's details.
     *
     * @return a formatted string of the person's details
     */
    @Override
    public String toString() {
        return "Person {" +
                "ID=" + id +
                ", First Name='" + firstName + '\'' +
                ", Last Name='" + lastName + '\'' +
                ", Department='" + department + '\'' +
                ", Performance Rating=" + performanceRating +
                ", Email='" + email + '\'' +
                '}';
    }
}
