package contacts;

import java.beans.Transient;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Main implements Serializable {

    private static final long serialVersionUID = 1L;

    ArrayList<Contact> contacts = new ArrayList<>();


    //private transient ObjectOutputStream oos;
    private String filename;
    private transient Path file;


    private void writeObject(ObjectOutputStream oos) throws IOException {
        System.out.println("writing object!");
        oos.defaultWriteObject();
    }


    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        System.out.println("Reading object: ");
        ois.defaultReadObject();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Main main = new Main();

        if (args.length > 0) {

            try {
                main.filename = args[0];
                String filename = main.filename;


                if (Files.exists(Path.of(filename))) {
                    main.file = Path.of(filename);

                    FileInputStream fileInputStream = new FileInputStream(String.valueOf(main.file));
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);


                    ObjectInputStream ois = new ObjectInputStream(bufferedInputStream);

                    Object obj = ois.readObject();

                    main.contacts = (ArrayList<Contact>) obj;
                    //System.out.println(obj.getClass().getName());
                    ois.close();
                    bufferedInputStream.close();
                    fileInputStream.close();


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (Files.exists(Path.of("default"))) {
                main.filename = "default";
                main.file = Paths.get("default");
            } else {
                File file = new File("default");
                file.createNewFile();
                main.file = Files.createFile(Paths.get("default"));
                main.filename = "default";

            }
        }

        InputStream in = System.in;
        BufferedReader br = new BufferedReader(new InputStreamReader(in));


        String action = "";
        while (!action.equals("exit")) {
            System.out.println("[menu] Enter action (add, list, search, count, exit): ");
            action = br.readLine().trim();

            // Switch cases look weird because I wrote enhanced switch statements but the testing requires
            // Java 11 so I had to refactor in a way that will make it easier to switch back.

            switch (action) {
                case "count": {
                    System.out.printf("The Phone Book has %d records.", main.contacts.size());
                    break;
                }
                case "edit": {
                    edit(main, br);
                    break;
                }
                case "remove": {
                    if (main.contacts.size() == 0) {
                        System.out.println("No records to remove!");
                    } else {
                        main.printAll();
                        System.out.println("Select a record: ");
                        String choice = br.readLine().trim();
                        int choiceInt = Integer.parseInt(choice) - 1;
                        main.contacts.remove(choiceInt);
                        System.out.println("The record removed!");
                    }
                    System.out.println();
                    break;
                }

                case "add": {
                    System.out.println("Enter the type (person, organization): ");
                    String type = br.readLine().trim();

                    if (Paths.get(main.filename) == null || !Files.exists(Paths.get(main.filename))) {
                        main.file = Files.createFile(Paths.get(main.filename));
                    }

                    FileOutputStream fileOutputStream = new FileOutputStream(String.valueOf(main.file));
                    BufferedOutputStream buff = new BufferedOutputStream(fileOutputStream);
                    ObjectOutputStream oos = new ObjectOutputStream(buff);

                    if (type.equals("person")) {
                        System.out.println("Enter the name: ");
                        String name = br.readLine().trim();

                        System.out.println("Enter the surname: ");
                        String surname = br.readLine().trim();

                        System.out.println("Enter the birth date: ");
                        String birthDate = br.readLine().trim();

                        if (birthDate.equals("")) {
                            System.out.println("Bad birth date!");
                            birthDate = "[no data]";
                        }

                        System.out.println("Enter the gender (M, F): ");
                        String gender = br.readLine().trim();
                        if (!gender.equals("M") && !gender.equals("F")) {
                            System.out.println("Bad gender!");
                            gender = "[no data]";
                        }

                        System.out.println("Enter the number: ");
                        String phoneNumber = br.readLine().trim();

                        Person contact = new Person();
                        contact.setName(name);
                        contact.setSurname(surname);
                        contact.setPhoneNumber(phoneNumber);
                        contact.setBirthday(birthDate);
                        contact.setGender(gender);

                        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
                        contact.setDateCreated(now.toString());
                        contact.setLastEdit(now.toString());

                        main.contacts.add(contact);

                        oos.writeObject(main.contacts);

                        System.out.println("The record added.");


                    } else if (type.equals("organization")) {
                        System.out.println("Enter the organization name: ");
                        String name = br.readLine().trim();

                        System.out.println("Enter the address: ");
                        String address = br.readLine().trim();

                        System.out.println("Enter the number: ");
                        String phoneNumber = br.readLine().trim();

                        Organization organization = new Organization();
                        organization.setName(name);
                        organization.setAddress(address);
                        organization.setPhoneNumber(phoneNumber);
                        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
                        organization.setDateCreated(now.toString());
                        organization.setLastEdit(now.toString());
                        main.contacts.add(organization);
                        oos.reset();
                        oos.writeObject(main.contacts);

                    }

                    oos.close();
                    buff.close();
                    fileOutputStream.close();
                    System.out.println();
                    break;
                }
                case "info":
                case "list": {
                    if (main.contacts.size() == 0) {
                        System.out.println("No records to list");
                    } else {
                        main.printAll();
                        System.out.println();

                        System.out.println("[list] Enter action ([number], back): ");
                        String choice = br.readLine().trim();
                        if (choice.matches("\\d+")) {
                            int index = Integer.parseInt(choice) - 1;

                            Contact contact = main.contacts.get(index);
                            System.out.println(contact);

                            System.out.println();
                            String recordAction = "";
                            while (!recordAction.equals("menu")) {
                                System.out.println("[record] Enter action (edit, delete, menu): ");
                                recordAction = br.readLine().trim();

                                switch (recordAction) {
                                    case "edit":
                                        System.out.println("Select a field " + contact.getFields() + ": ");
                                        String field = br.readLine().trim();
                                        if (contact.getFields().contains(field)) {
                                            System.out.println("Enter " + field + ": ");
                                        } else {
                                            continue;
                                        }

                                        String newValue = br.readLine().trim();
                                        contact.changeField(field, newValue);
                                        System.out.println("Saved");
                                        System.out.println(contact);
                                        System.out.println();
                                        break;
                                    case "delete":
                                        //put remove here
                                        break;
                                }
                            }
                            }
                    }
                    System.out.println();
                    break;
                }
                case "search": {

                    extracted(main, br);

                    String searchAction = "";
                    while (!searchAction.equals("back") && !searchAction.equals("menu")) {
                        System.out.println("[search] Enter action ([number], back, again): ");
                        searchAction = br.readLine().trim();

                        if (searchAction.equals("again")) {
                            extracted(main, br);
                        } else if (searchAction.matches("\\d+")) {
                            try {
                                System.out.println(main.contacts.get(Integer.parseInt(searchAction)).toString());
                                System.out.println();
                            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }
                //
            }
        }
    }

    private static void edit(Main main, BufferedReader br) throws IOException {
        Contact contact;
        if (main.contacts.size() == 0) {
            System.out.println("No records to edit!");
        } else {

            if (main.contacts.size() > 1) {
                main.printAll();
                System.out.println("Select a record: ");
                String choice = br.readLine().trim();
                if (!choice.matches("\\d*")) {
                    return;
                }
                int choiceInt = Integer.parseInt(choice) - 1;
                contact = main.contacts.get(choiceInt);
            } else {
                contact = main.contacts.get(0);
            }


            System.out.println("Select a field " + contact.getFields() + ": ");
            String field = br.readLine().trim();
            System.out.println("Enter " + field + ": ");
            String value = br.readLine().trim();
            contact.changeField(field, value);
            contact.setLastEdit(LocalDateTime.now().toString());
            System.out.println("The record updated!");

            FileOutputStream fileOutputStream = new FileOutputStream(String.valueOf(main.file));
            ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
            oos.writeObject(main.contacts);
            oos.close();
            fileOutputStream.close();

        }
        System.out.println();
    }

    private static void extracted(Main main, BufferedReader br) throws IOException {
        System.out.println("Enter search query: ");
        String query = br.readLine().toLowerCase(Locale.ROOT);

        List<Contact> results = main.contacts.stream().filter(contact -> {
            String values = contact.getValues().toLowerCase(Locale.ROOT);
            return values.contains(query) || values.matches(".*" + query + ".*");
        }).sorted(Comparator.comparing(Contact::getName)).collect(Collectors.toList());

        System.out.printf("Found %d results: \n", results.size());
        var ref = new Object() {
            int i = 1;
        };
        results.forEach( contact -> {
            System.out.println(ref.i + ". " + contact.getFullName());
            ref.i++;
        });
        System.out.println();
    }

    public void printPerson(Person person) {
            System.out.println((contacts.indexOf(person) + 1) + ". " + person.getName() + " " + person.getSurname());
    }

    public void printOrganization(Organization organization) {
        System.out.println((contacts.indexOf(organization) + 1) + ". " + organization.getName());
    }

    public void printAll() {
        for (Object c: contacts) {
            if (c instanceof Person) {
                printPerson((Person) c);
            } else {
                printOrganization((Organization) c);
            }
        }
    }

    public Main() {
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
    }
}
