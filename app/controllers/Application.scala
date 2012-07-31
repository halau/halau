package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._

object Application extends Controller {
  def index = Action {
    val xs = ItemDB.findAll
    Ok(views.html.index(moneyForm.fill(Item(0, None, None)), xs))
  }

  def addMoney = Action { implicit req =>
    val item = moneyForm.bindFromRequest.get
    ItemDB.add(item)
    Redirect(routes.Application.index())
    }

  val moneyForm = Form(
	mapping(
	"money" -> number,
    "location" -> optional(text),
    "kind" -> optional(text)
    )(Item.apply)(Item.unapply)
  )
}
