import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum ReactionStates {
    STARTING(Arrays.asList()),
    WAITING(Arrays.asList("STARTING", "ERROR", "REACTED")),
    REACTING(Arrays.asList("WAITING")),
    REACTED(Arrays.asList("REACTING")),
    ERROR(Arrays.asList("WAITING"));

    private List<String> validFromStates = new LinkedList<>();

    ReactionStates(List<String> validFromStates) {
        this.validFromStates = validFromStates;
    }

    public boolean canChangeReactionState(ReactionStates toState) {
        return toState.validFromStates.contains(this.name());
    }
}
