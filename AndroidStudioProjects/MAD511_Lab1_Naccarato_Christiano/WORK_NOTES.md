Step (9): E  Could not create Worker com.example.mad511_lab1_naccarato_christiano.work.BackupWorker

WorkManager started before Hilt was ready so it couldn't give my worker its repository 
and fail to create it.

Step (3): I used the first value of observeAll() because a backup only needs one snapshot.

Why I chose each constraint:
Storage not low: the backup writes a file and on a nearly full phone the write could fail. 
Battery not low: background backup and cleanup work could drain a nearly dead battery.

What happens if the backup worker runs twice in a row:

It is safe because each run writes a new timestamped file without touching the database or older files.

one thing that could make a periodic job run later than its interval:

The constraints may not be met when it is due, so WorkManager waits until they are.