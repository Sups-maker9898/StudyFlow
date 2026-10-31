# StudyFlow – Study & Assignment Manager (starter project)

Android Studio starter built from `MyNavigationAPP` for the midterm design
"StudyFlow". It covers the weeks 9–12 plan: four screens, Navigation graph,
bottom navigation and RecyclerView lists. Data is **in memory** for now.
Room is added in weeks 13–14 (see below).

## Open it
Android Studio → File → Open → select this `StudyFlow` folder → let Gradle sync → Run ▶.

## Screens (Fragments)
| Screen | File | Role |
|---|---|---|
| Assignment list (start) | `ui/AssignmentListFragment.kt` | RecyclerView, All/Pending/Done chips, + button |
| Assignment detail | `ui/AssignmentDetailFragment.kt` | Complete / Edit / Delete |
| Add / edit assignment | `ui/AddEditAssignmentFragment.kt` | Form, date picker, validation |
| Course list | `ui/CourseListFragment.kt` | RecyclerView, add/edit course dialog |

Navigation graph: `res/navigation/nav_graph.xml`. The bottom bar switches Assignments ↔ Courses.

## Data layer (`data/`)
- `Models.kt` – `Course`, `Assignment` (same fields as the planned Room tables)
- `StudyRepository.kt` – in-memory store with sample data; method names mirror the planned DAOs
- `DateUtils.kt` – D-day label and "urgent" check (due in ≤ 3 days or overdue → red)

## Next steps: add Room
1. Add the Room + KSP dependencies (check the versions that match your AGP).
2. Turn `Course` / `Assignment` into `@Entity` classes (`courses`, `assignments`, FK `courseId`, CASCADE).
3. Create `CourseDao`, `AssignmentDao`, `AppDatabase` (`study_flow_database`).
4. Replace the bodies of `StudyRepository` functions with DAO calls inside coroutines
   and expose lists as `Flow`/`LiveData`.
