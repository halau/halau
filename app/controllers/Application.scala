package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._

object Application extends Controller {
  def index = Action {
    val xs = ItemDB.findAll
    Ok(views.html.index(moneyForm.fill(Item(-1,0, None, None)), xs))
  }

  def addMoney = Action { implicit req =>
    val item = moneyForm.bindFromRequest.get
    ItemDB.add(item)
    Redirect(routes.Application.index())
  }

  def edit(id : Int) = Action {
    val item = ItemDB.find(id)
    item match {
      case Some(item) =>
        Ok(views.html.edit(id, moneyForm.fill(item)))
      case None =>
        NotFound("")
    }
  }
  
  def update(id : Int) = Action { implicit req =>
    val item = moneyForm.bindFromRequest.get.copy(id = id)
    ItemDB.update(item)
    Redirect(routes.Application.index())
  }
  def destroy(id : Int) = Action {
    ItemDB.remove(id)
    Redirect(routes.Application.index())
  }

  val moneyForm = Form(
     mapping(
       "money" -> number,
       "location" -> optional(text),
       "kind" -> optional(text)
     )((m,l,k) => Item(-1, m,l, k))((m) => Some(m.money, m.location, m.kind))
  )
}
