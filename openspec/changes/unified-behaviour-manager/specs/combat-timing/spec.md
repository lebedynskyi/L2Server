## MODIFIED Requirements

### Requirement: Attack scheduling is cancellable via a single handle
The system SHALL expose one cancellation handle per attacking creature, owned by the shared behaviour-coordination entry point (not by a per-action manager), that when cancelled stops any pending or future-rescheduled attack hit for that creature.

#### Scenario: Changing intention cancels the pending attack
- **WHEN** a creature's current intention is `ATTACK` with a pending scheduled hit, and any action is started for that creature through the shared coordination point
- **THEN** the previously scheduled hit task is cancelled before the new intention takes effect, without the new action's code needing to call an attack-specific cancellation method directly

#### Scenario: Target no longer valid stops the reschedule chain
- **WHEN** a scheduled hit executes and the creature is no longer attacking the same target (target dead, out of range, or intention changed away from `ATTACK`)
- **THEN** the task does not reschedule itself and no further hits occur for that attack session
