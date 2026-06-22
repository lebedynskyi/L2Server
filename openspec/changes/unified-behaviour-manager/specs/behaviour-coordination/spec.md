## ADDED Requirements

### Requirement: Action outcomes are reported via a 3-state result
The system SHALL represent the outcome of evaluating a creature's in-progress action (movement, attack, or future skill/cast) as one of exactly three states — in progress, finished naturally, or interrupted — so that "naturally finished" and "superseded by another action" are never conflated.

#### Scenario: Action still running
- **WHEN** an in-progress action's condition for completion is not yet met and it has not been superseded
- **THEN** the result is `IN_PROGRESS`

#### Scenario: Action completes naturally
- **WHEN** an in-progress action reaches its own completion condition (e.g. movement reaches its destination, an attack's target becomes invalid after a resolved hit)
- **THEN** the result is `FINISHED`

#### Scenario: Action superseded by another
- **WHEN** a creature's current intention no longer matches the action that is being evaluated (something else already changed it)
- **THEN** the result is `INTERRUPTED`

### Requirement: Queue advancement happens only on natural completion
The system SHALL advance a creature's queued next intention (`behaviour.next` becoming `behaviour.current`) only when an action's result is `FINISHED`, and SHALL NOT advance the queue when an action's result is `INTERRUPTED`.

#### Scenario: Finished action advances the queue
- **WHEN** an action's evaluation result is `FINISHED` and the creature has a queued next intention
- **THEN** the queued intention becomes the current intention

#### Scenario: Interrupted action does not touch the queue
- **WHEN** an action's evaluation result is `INTERRUPTED`
- **THEN** the creature's current/next intention is left exactly as whatever superseded it set, with no further mutation

### Requirement: Starting any action cancels the creature's previous action through one shared entry point
The system SHALL provide a single coordination point that, when starting a new action for a creature, cancels whatever action (tick-based or schedule-based) was previously running for that creature, without requiring the new action's code to know which specific action type preceded it.

#### Scenario: Starting attack cancels in-progress movement
- **WHEN** a creature is moving and an attack is started for that creature through the shared coordination point
- **THEN** the movement is stopped before the attack's intention and scheduling take effect

#### Scenario: Starting movement cancels an in-progress schedule-based action
- **WHEN** a creature has a pending scheduled action (e.g. attack) and movement is started for that creature through the shared coordination point
- **THEN** the pending scheduled action is cancelled before the movement's intention takes effect

#### Scenario: New action types do not require pairwise wiring
- **WHEN** a new schedule-based action type is introduced
- **THEN** it cancels and is cancelled by existing action types solely by going through the shared coordination point, with no new direct call from/to any other specific action manager
