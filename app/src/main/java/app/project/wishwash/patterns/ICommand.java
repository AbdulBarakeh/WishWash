package app.project.wishwash.patterns;

public interface ICommand {//Guidance from AU547760. It is used as a OnSuccessListener, to make sure that some dependent code is run.
    public void Handle (Object data);
}
