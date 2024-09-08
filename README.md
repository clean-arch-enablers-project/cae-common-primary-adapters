# ✔️ cae-common-primary-adapters
☕ Java & Kotlin edition

<br>

Welcome to the open-source CAE Common Primary Adapters repository! This SDK module is designed to provide a collection of commonly used primary adapters — components that trigger the execution of use cases. It serves as a convenient starting point for creating these software components, offering seamless integration with other CAE components by default. 

In this repository, each folder corresponds to a specific common primary adapter. All of them share the prefix 'cae-cpa', which stands for 'CAE (Clean Arch Enablers) Common Primary Adapters', followed by the name of the actual component.

<br>

State Symbol Key:

- ``✅`` — _Under release state_
- ``✔️`` — _Under snapshot state_
- ``⏳`` — _Under full development state_

<br>

## ``✔️`` cae-cpa-command-controller
This component provides a framework for building CLI tools. You can declare commands, define their parameters, and use them to dispatch executable actions within the CLI process.

### ▶️ The artifact:

```xml
<dependency>
    <groupId>com.clean-arch-enablers</groupId>
    <artifactId>command-controller</artifactId>
    <version>${version}</version>
</dependency>
```

### ✍️ Examples

#### Registering the name and the version of the CLI tool to display and the commands to accept.

With this script the CLI app would be set to display the name as ``cae-cli``, its version as ``1.0.0``. The commands accepted would be ``NewFunctionUseCaseCommand``, ``NewConsumerUseCaseCommand``, ``NewSupplierUseCaseCommand``, ``NewRunnableUseCaseCommand``, ``NewProjectCommand``, ``RotateToMonolayerCommand``.

```java
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CaeCliBootstrapSettings {

    public static void run(){
        ClientSettingsProvider.SINGLETON.setClientName("cae-cli");
        ClientSettingsProvider.SINGLETON.setVersion("1.0.0");
        CommandRepository.SINGLETON.registerNewCommands(List.of(
                new NewFunctionUseCaseCommand(),
                new NewConsumerUseCaseCommand(),
                new NewSupplierUseCaseCommand(),
                new NewRunnableUseCaseCommand(),
                new NewProjectCommand(),
                new RotateToMonolayerCommand()
        ));
    }

}
```

#### Declaring a command

With this script the ``NewProjectCommand`` class becomes a ``Command``. Within its constructor 4 command parameters are declared: ``monolayer`` (a flag, optional), ``artifactId`` (required parameter), ``groupId`` (required parameter) and ``caeVersion`` (required parameter).

Whenever the command gets executed, its flow is whatever is inside its ``applyInternalLogic`` method. In this instance, the logic is to call the ``GenerateProjectStructureUseCase`` which is an API from the 
[cae-meta-structure](https://github.com/clean-arch-enablers-project/cae-meta-structure) library.


```java
public class NewProjectCommand extends Command {

    public NewProjectCommand() {
        super("new-project");
        this.registerParameter("monolayer", CommandParameterDefinitions.newOptionalFlagParameter());
        this.registerParameter("artifactId", CommandParameterDefinitions.newRequiredDefaultParameter());
        this.registerParameter("groupId", CommandParameterDefinitions.newRequiredDefaultParameter());
        this.registerParameter("caeVersion", CommandParameterDefinitions.newRequiredDefaultParameter());
    }

    @Override
    protected void applyInternalLogic() {
        var isMonolayer = this.getCommandParameters().get("monolayer").isPresent();
        var artifactId = this.getCommandParameters().get("artifactId").getActualValue();
        var groupId = this.getCommandParameters().get("groupId").getActualValue();
        var frameworkVersion = this.getCommandParameters().get("caeVersion").getActualValue();
        var useCaseInput = GenerateProjectStructureUseCaseInput.builder()
                .artifactId(artifactId)
                .groupId(groupId)
                .monolayer(isMonolayer)
                .caeFrameworkVersion(frameworkVersion)
                .build();
        var useCase = new GenerateProjectStructureUseCaseImplementation();
        useCase.execute(useCaseInput, UseCaseExecutionCorrelation.ofNew());
    }
}

```

#### Dispatching for the Main class of the CLI app

After declaring and registering your commands in the ``CommandRepository``, you can dispatch them by following the script below in the main class of your CLI application.


```java
public class CaeCli {

    public static void main(String[] args) {
        CaeCliBootstrapSettings.run();
        CommandController.serve(CommandRequest.of(args));
    }

}
```
