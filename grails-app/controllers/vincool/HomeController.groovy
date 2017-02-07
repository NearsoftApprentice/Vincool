package vincool

class HomeController {
    def roleUserService

    def index() {
        if (roleUserService.isCurrentUserAnAttendee()) {
            forward controller: "event"
        } else if (roleUserService.isCurrentUserAInstructor()) {
            forward controller: "event"
        } else if (roleUserService.isCurrentUserAnAdmin()) {
            forward controller: "event"
        } else {
            render(view: "/landing")
        }

    }
}
