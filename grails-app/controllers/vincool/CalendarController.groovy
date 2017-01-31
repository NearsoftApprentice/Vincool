package vincool

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.web.mapping.LinkGenerator

@Secured(['permitAll'])
class CalendarController {

    static defaultAction = "index"
    static allowedMethods = [index: "GET", detail: "GET"]

    private LinkGenerator grailsLinkGenerator
    def springSecurityService

    def index() {
        def sessions = Session.findAllByBatchInList(Batch.findAllByIsActive(true))
        def offices = Office.all

        def source = [:]
        offices.each { office ->
            source[office.officeCode] = [].withDefault{ [:] }
        }

        def sessionsEnrolledMap = [:]
        if(isCurrentUserAStudent()) {
            def sessionsEnrolled = Enrollment.findAllByStudent(springSecurityService.getCurrentUser())
            sessionsEnrolled.each { session ->
                sessionsEnrolledMap[session.session] = session.student;
            }
        }

        def color
        sessions.each{ session ->
            color = "orange"
            if(sessionsEnrolledMap[session] != null) { color = "gray" }
            source[session.office.officeCode]
                    .add([id: session.id,
                          title: session.lesson.topic,
                          start: session.date,
                          allDay: false,
                          color: color,
                          url: grailsLinkGenerator.link(controller: "calendar", action: "detail", id: session.id, absolute: true)])
        }

        [source: source as JSON, offices: offices]
    }

    def detail(Long id) {

        def session = Session.get(id)
        if (session == null) {
            redirect(action: "calendar")
        }

        def isEnrolled = false

        if(isCurrentUserAStudent()) {
            isEnrolled = Enrollment.findByStudentAndSession(springSecurityService.getCurrentUser(), session) != null
        }

        [sessionDetails: session, isEnrolled: isEnrolled]
    }

    private boolean isCurrentUserAStudent() {
        if (springSecurityService.isLoggedIn()) {
            def roles = springSecurityService.getPrincipal().getAuthorities()
            def rolesNames = roles.collect { it.getAuthority() }
            if (rolesNames.contains("ROLE_STUDENT")) { return true }
        }
        return false
    }

}
