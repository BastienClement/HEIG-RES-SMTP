# HEIG-RES-SMTP

This project is an implementation of the SMTP assignment for the RES (Networking) course at HEIG-VD.

https://github.com/SoftEng-HEIGVD/Teaching-HEIGVD-RES-2016-Labo-SMTP

## Objectives

Given a list of prank targets, we must split them into groups of approximately the same size and designate one the victim as "the sender". We will then send a fake email to every other victim in the same group with a random subject and body, using the person selected in the first step as the sender address. One or more witnesses should also receive a copy of the prank message (as Cc:).

The SMTP client implementation had to bed done by us and we used a local mock server to test our implementation without actually disturbing prank victims.

## Usage

Simply run the `prank.jar` file.

```
$ java -jar prank.jar
```
Configuration is available in the `config/` directory. You can customize:

- SMTP server address and port
- Number of groups to build
- Message templates
- Prank victims

Message templates are defined in the `config/messages.txt` file. The first line of each message will be used as the subject and the end of each message must be marked by a `::END::` tag (even for the last one).

Prank victims are defined in `config/victims.txt`, with one victim per line.

If you just want to test the application without actually sending anything, you can use a mock SMTP server like MockMock: https://github.com/tweakers-dev/MockMock

The MockMock jar is already included in this repository, simply run

```
$ java -jar MockMock.jar
```

and you will have a fake SMTP server listening on port 25. Please note that you usually need to be root to bind something to this port, you may need to use `sudo` if MockMock complains about not being able to bind.

## Application structure

![](https://github.com/galedric/HEIG-RES-SMTP/raw/master/figures/diagram.png)

I chose to keep the application structure very simple. I personally find the suggested structure a bit overkill for the simplicity of this project (most notably the interface / implementation separation). There was no need for inheritance or any fancy OO stuff. Just a bunch of classes that encapsulate states and behaviors.

- The **Main** class creates a single ConfigManager and Sender for the lifetime of the application. It then builds groups of victims by using the `Group.buildGroups` method and for each of them selects and sends a random message from a template.
- The **ConfigManager** is responsible for reading configuration files and make settings available for the Main class.
- The **Sender** is the implementation of the SMTP protocol. It is bound to the server given in the config file and provides the `.send(Message)` method to send a new mail.
- A **MessageTemplate** is a template for new messages. It contains a subject and a body but is missing the sender and recipients addresses.
- A **Message** is created from a template and is a complete message, ready to be sent by the Sender. It contains sender and recipients addresses in addition to the subject and body from the original template.
- A **Group** is a set of victims of the prank. Each group has at least one sender and two victims.

## Example

![](https://github.com/galedric/HEIG-RES-SMTP/raw/master/figures/screenshot.png)
